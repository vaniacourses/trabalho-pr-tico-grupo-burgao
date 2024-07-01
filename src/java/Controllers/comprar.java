/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DAO.DaoBebida;
import DAO.DaoCliente;
import DAO.DaoLanche;
import DAO.DaoPedido;
import Helpers.ValidadorCookie;
import Model.Bebida;
import Model.Cliente;
import Model.Lanche;
import Model.Pedido;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.management.InvalidAttributeValueException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kener_000
 */
public class comprar extends HttpServlet {

    public Pedido registrarPedido(JSONObject dados,
                                  Cliente cliente,
                                  DaoBebida bebidaDao,
                                  DaoLanche lancheDao,
                                  DaoPedido pedidoDao) throws InvalidAttributeValueException, JSONException {
        Double valor_total = 0.00;

        List<Lanche> lanches = new ArrayList<>();
        List<Bebida> bebidas = new ArrayList<>();

        Iterator<String> keys = dados.keys();

        while(keys.hasNext()) {

            String nome = keys.next();
            if(!nome.equals("id")){

                int quantidade = dados.getJSONArray(nome).getInt(2);

                if (quantidade == 0) {
                    throw new InvalidAttributeValueException("Quantidade não pode ser 0");
                }

                if(dados.getJSONArray(nome).get(1).equals("lanche")){
                    Lanche lanche = lancheDao.pesquisaPorNome(nome);
                    lanche.setQuantidade(quantidade);
                    valor_total += lanche.getValor_venda() * quantidade;
                    lanches.add(lanche);
                }

                if(dados.getJSONArray(nome).get(1).equals("bebida")){
                    Bebida bebida = bebidaDao.pesquisaPorNome(nome);
                    bebida.setQuantidade(quantidade);
                    valor_total += bebida.getValor_venda() * quantidade;
                    bebidas.add(bebida);
                }
            }
        }

        Pedido pedido = new Pedido();
        pedido.setData_pedido(Instant.now().toString());
        pedido.setCliente(cliente);
        pedido.setValor_total(valor_total);
        pedidoDao.salvar(pedido);
        pedido = pedidoDao.pesquisaPorData(pedido);

        vinculaBebidasELanches(lanches, bebidas, pedido, pedidoDao);

        return pedido;
    }

    private void vinculaBebidasELanches(List<Lanche> lanches, List<Bebida> bebidas, Pedido pedido, DaoPedido daoPedido) {
        for(int i = 0; i<lanches.size(); i++){
            daoPedido.vincularLanche(pedido, lanches.get(i));
        }

        for(int i = 0; i<bebidas.size(); i++){
            daoPedido.vincularBebida(pedido, bebidas.get(i));
        }
    }
}
