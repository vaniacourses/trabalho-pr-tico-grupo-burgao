package DAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Pedido;

public class DaoPedidoTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private DaoPedido daoPedido;

    @BeforeEach
    public void setUp() {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        daoPedido = new DaoPedido(mockConnection);
    }

    @Test
    public void testSalvar() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        Pedido pedido = new Pedido();
        pedido.setCliente(new Cliente());
        pedido.getCliente().setId_cliente(1);
        pedido.setData_pedido("2024-05-");
        pedido.setValor_total(100.0);

        daoPedido.salvar(pedido);

        verify(mockStatement, times(1)).execute();
    }

    @Test
    public void testRetornaExceptionQuandoDataIsNull() throws SQLException {
            Pedido pedido = new Pedido();
            pedido.setCliente(new Cliente());
            pedido.getCliente().setId_cliente(1);
            pedido.setData_pedido(null);
            pedido.setValor_total(100.0);

            assertThrows(RuntimeException.class, () -> daoPedido.salvar(pedido));
    }

    @Test
    public void testGetPedidoPorData() throws SQLException {
        var pedido = new Pedido();
        var cliente = new Cliente();
        cliente.setId_cliente(1);
        pedido.setCliente(cliente);
        pedido.setId_pedido(1);
        pedido.setData_pedido("2024-05-01");
        pedido.setValor_total(55.9);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id_pedido")).thenReturn(pedido.getId_pedido());
        when(mockResultSet.getString("data_pedido")).thenReturn(pedido.getData_pedido());
        when(mockResultSet.getDouble("valor_total")).thenReturn(pedido.getValor_total());

        var pedidoPersistido = daoPedido.pesquisaPorData(pedido);

        assertEquals(pedido.getId_pedido(), pedidoPersistido.getId_pedido());
        assertEquals(pedido.getData_pedido(), pedidoPersistido.getData_pedido());
        assertEquals(pedido.getValor_total(), pedidoPersistido.getValor_total());
    }
}
