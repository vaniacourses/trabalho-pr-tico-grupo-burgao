package Controllers;

import DAO.DaoBebida;
import DAO.DaoLanche;
import DAO.DaoPedido;
import Model.Bebida;
import Model.Cliente;
import Model.Lanche;
import Model.Pedido;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.management.InvalidAttributeValueException;

public class ComprarTest {

    @Mock
    private DaoBebida daoBebida;
    @Mock
    private DaoLanche daoLanche;
    @Mock
    private DaoPedido daoPedido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarPedidoValorTotalCorreto() throws InvalidAttributeValueException, JSONException {
        int quantidadeLanche1 = 2;
        int quantidadeBebida1 = 3;
        String jsonStr = String
                .format("{\"id\": 1,\"lanche1\": [\"lanche1\", \"lanche\", %d], \"bebida1\": [\"bebida1\", \"bebida\", %d]}",
                        quantidadeLanche1,
                        quantidadeBebida1);
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();
        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();
        Pedido pedido = comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido);

        Double valor_total_esperado = quantidadeLanche1 * lanche1.getValor_venda();
        valor_total_esperado += quantidadeBebida1 * bebida1.getValor_venda();

        assertEquals(valor_total_esperado, pedido.getValor_total());
    }

    @Test
    void testRegistrarPedidoValorTotalQuantidade1Correto() throws InvalidAttributeValueException, JSONException {
        int quantidadeLanche1 = 1;
        int quantidadeBebida1 = 1;
        String jsonStr = String
                .format("{\"id\": 1,\"lanche1\": [\"lanche1\", \"lanche\", %d], \"bebida1\": [\"bebida1\", \"bebida\", %d]}",
                        quantidadeLanche1,
                        quantidadeBebida1);
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();
        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();
        Pedido pedido = comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido);

        Double valor_total_esperado = quantidadeLanche1 * lanche1.getValor_venda();
        valor_total_esperado += quantidadeBebida1 * bebida1.getValor_venda();

        assertEquals(valor_total_esperado, pedido.getValor_total());
    }

    @Test
    void testRegistrarPedidoLanche1QuantidadeIgual0() {
        int quantidadeLanche1 = 0;
        int quantidadeBebida1 = 1;
        String jsonStr = String
                .format("{\"id\": 1,\"lanche1\": [\"lanche1\", \"lanche\", %d], \"bebida1\": [\"bebida1\", \"bebida\", %d]}",
                        quantidadeLanche1,
                        quantidadeBebida1);
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();

        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();

        assertThrows(InvalidAttributeValueException.class,
                () -> comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido));
    }

    @Test
    void testRegistrarPedidoBebida1QuantidadeIgual0() {
        int quantidadeLanche1 = 1;
        int quantidadeBebida1 = 0;
        String jsonStr = String
                .format("{\"id\": 1, \"lanche1\": [\"lanche1\", \"lanche\", %d], \"bebida1\": [\"bebida1\", \"bebida\", %d]}",
                        quantidadeLanche1,
                        quantidadeBebida1);
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();

        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();

        assertThrows(InvalidAttributeValueException.class,
                () -> comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido));
    }

    @Test
    void testRegistrarPedidoJsonSemQuantidade() {
        String jsonStr = "{\"id\": 1,\"lanche1\": [\"lanche1\", \"lanche\"], \"bebida1\": [\"bebida1\", \"bebida\"]}";
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();

        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();

        assertThrows(JSONException.class,
                () -> comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido));
    }

    @Test
    void testRegistrarPedidoJsonInvalido() {
        String jsonStr = "{\"id\": 1, \"lanche1\": [], \"bebida1\": []}";
        JSONObject dados = new JSONObject(jsonStr);

        Bebida bebida1 = getBebida1();
        Lanche lanche1 = getLanche1();

        setLancheBebidaPedidoMocks(bebida1, lanche1);
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        comprar comprarServlet = new comprar();

        assertThrows(JSONException.class,
                () -> comprarServlet.registrarPedido(dados, cliente, daoBebida, daoLanche, daoPedido));
    }

    private void setLancheBebidaPedidoMocks(Bebida bebida1, Lanche lanche1) {
        when(daoBebida.pesquisaPorNome("bebida1")).thenReturn(bebida1);
        when(daoLanche.pesquisaPorNome("lanche1")).thenReturn(lanche1);

        doAnswer(new Answer<Pedido>() {
            @Override
            public Pedido answer(InvocationOnMock invocation) throws Throwable {
                Pedido pedido = invocation.getArgument(0);
                return pedido;
            }
        }).when(daoPedido).pesquisaPorData(any(Pedido.class));
    }

    private Bebida getBebida1() {
        Bebida bebida1 = new Bebida();
        bebida1.setId_bebida(1);
        bebida1.setNome("bebida1");
        bebida1.setValor_venda(5.0);
        return bebida1;
    }

    private Lanche getLanche1() {
        Lanche lanche1 = new Lanche();
        lanche1.setId_lanche(1);
        lanche1.setNome("lanche1");
        lanche1.setValor_venda(20.5);
        return lanche1;
    }
}