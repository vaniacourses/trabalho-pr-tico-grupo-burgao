package DAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DAO.DaoPedido;
import Model.Bebida;
import Model.Lanche;
import Model.Pedido;

public class DaoPedidoTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private DaoPedido daoPedido;

    @BeforeEach
    public void setUp() {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        daoPedido = new DaoPedido(mockConnection);
    }

    @Test
    public void testSalvar() {
        Pedido pedido = new Pedido();
        pedido.getCliente().setId_cliente(1);
        pedido.setData_pedido("2024-05-08");
        pedido.setValor_total(50.0);

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(mockPreparedStatement).setString(anyInt(), anyString());
        doNothing().when(mockPreparedStatement).setDouble(anyInt(), anyDouble());
        doNothing().when(mockPreparedStatement).execute();
        doNothing().when(mockPreparedStatement).close();

        daoPedido.salvar(pedido);

        verify(mockPreparedStatement).execute();
    }

    @Test
    public void testVincularLanche() {
        Pedido pedido = new Pedido();
        Lanche lanche = new Lanche();
        lanche.setId_lanche(1);
        lanche.setQuantidade(2);

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(mockPreparedStatement).execute();
        doNothing().when(mockPreparedStatement).close();

        daoPedido.vincularLanche(pedido, lanche);

        verify(mockPreparedStatement).execute();
    }

    @Test
    public void testVincularBebida() {
        Pedido pedido = new Pedido();
        Bebida bebida = new Bebida();
        bebida.setId_bebida(1);
        bebida.setQuantidade(2);

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        doNothing().when(mockPreparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(mockPreparedStatement).execute();
        doNothing().when(mockPreparedStatement).close();

        daoPedido.vincularBebida(pedido, bebida);

        verify(mockPreparedStatement).execute();
    }

    @Test
    public void testPesquisaPorData() {
        Pedido pedido = new Pedido();
        pedido.setData_pedido("2024-05-08");

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id_pedido")).thenReturn(1);
        when(mockResultSet.getString("data_pedido")).thenReturn("2024-05-08");
        when(mockResultSet.getDouble("valor_total")).thenReturn(50.0);
        doNothing().when(mockResultSet).close();
        doNothing().when(mockPreparedStatement).close();

        Pedido resultado = daoPedido.pesquisaPorData(pedido);

        verify(mockResultSet).next();
        assertNotNull(resultado);
        assertEquals(1, resultado.getId_pedido());
        assertEquals("2024-05-08", resultado.getData_pedido());
        assertEquals(50.0, resultado.getValor_total(), 0.001);
    }
}
