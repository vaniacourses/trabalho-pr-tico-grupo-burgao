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

    private static final Logger logger = Logger.getLogger(comprar.class.getName());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        
        ////////Validar Cookie
        boolean resultado = false;
        
        try{
        Cookie[] cookies = request.getCookies();
        ValidadorCookie validar = new ValidadorCookie();

        resultado = validar.validar(cookies);
        } catch(java.lang.NullPointerException e) {
            logger.info(e.toString());
        }

        if (resultado) {
            json = br.readLine();
            byte[] bytes = json.getBytes(ISO_8859_1); 
            String jsonStr = new String(bytes, UTF_8);            
            JSONObject dados = new JSONObject(jsonStr);
            
            DaoCliente clienteDao = new DaoCliente(); 
            
            Cliente cliente = clienteDao.pesquisaPorID(String.valueOf(dados.getInt("id")));
            
            if (cliente != null) {
                try {
                    var pedido = registrarPedido(dados, cliente, new DaoBebida(), new DaoLanche(), new DaoPedido());
                    try (PrintWriter out = response.getWriter()) {
                        out.println("Pedido realizado com sucesso!");
                        out.println("Obrigado, " + pedido.getCliente().getNome());
                    }
                } catch (InvalidAttributeValueException | JSONException e) {
                    logger.info(e.toString());
                }
            } else {
                try (PrintWriter out = response.getWriter()) {
                    out.println("Usuário não encontrado!");
                }
            }

        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("erro");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


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
                    valor_total += lanche.getValor_venda();
                    lanches.add(lanche);
                }

                if(dados.getJSONArray(nome).get(1).equals("bebida")){
                    Bebida bebida = bebidaDao.pesquisaPorNome(nome);
                    bebida.setQuantidade(quantidade);
                    valor_total += bebida.getValor_venda();
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
        pedido.setCliente(cliente);

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
