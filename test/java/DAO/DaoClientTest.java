import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import DAO.DaoCliente;
import Helpers.EncryptadorMD5;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Cliente;
import DAO.DaoEndereco;

@ExtendWith(MockitoExtension.class)
public class DaoClienteTest {

    @Mock
    private Connection conexaoMock;

    @Mock
    private EncryptadorMD5 encryptadorMD5Mock;

    @InjectMocks
    private DaoCliente daoCliente;

    @Test
    public void testCriacaoCliente() {
        Cliente cliente = new Cliente("João", "Silva", "1234567890", "joaosilva", "senha", 1);
        assertNotNull(cliente);
    }

    @Test
    public void testSalvarCliente() throws SQLException {
        Cliente cliente = new Cliente("João", "Silva", "1234567890", "joaosilva", "senha", 1);
        DaoEndereco daoEnderecoMock = Mockito.mock(DaoEndereco.class);
        Mockito.when(daoEnderecoMock.validaEndereco(cliente.getEndereco())).thenReturn(1);
        daoCliente.setDaoEndereco(daoEnderecoMock);

        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        daoCliente.salvar(cliente);

        PreparedStatement preparedStatement = Mockito.verify(conexaoMock).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, cliente.getNome());
        Mockito.verify(preparedStatement).setString(2, cliente.getSobrenome());
        Mockito.verify(preparedStatement).setString(3, cliente.getTelefone());
        Mockito.verify(preparedStatement).setString(4, cliente.getUsuario());
        Mockito.verify(preparedStatement).setString(5, cliente.getSenha());
        Mockito.verify(preparedStatement).setInt(6, cliente.getFg_ativo());
        preparedStatement.executeUpdate();
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testListarTodos() throws SQLException {
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        daoCliente.listarTodos();

        Mockito.verify(conexaoMock).prepareStatement(Mockito.anyString());
    }

    @Test
    public void testPesquisaPorUsuario() throws SQLException {
        Cliente cliente = new Cliente("joaosilva", "senha", 1);
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        daoCliente.pesquisaPorUsuario(cliente);

        PreparedStatement preparedStatement = Mockito.verify(conexaoMock).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, cliente.getUsuario());
    }

    @Test
    public void testPesquisaPorID() throws SQLException {
        String id = "1";
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        daoCliente.pesquisaPorID(id);

        PreparedStatement preparedStatement = Mockito.verify(conexaoMock).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, id);
    }

    @Test
    public void testLoginSucesso() throws SQLException {
        String nomeUsuario = "joaosilva";
        String senha = "senha";
        Cliente cliente = new Cliente(nomeUsuario, senha, 1);
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        Mockito.when(encryptadorMD5Mock.encryptar(senha)).thenReturn("senhaCriptografada");
        Mockito.when(daoCliente.pesquisaPorUsuario(cliente)).thenReturn(cliente);

        boolean resultadoLogin = daoCliente.login(cliente);
        assertTrue(resultadoLogin);
    }

    @Test
    public void testLoginNomeUsuarioInvalido() throws SQLException {
        String nomeUsuario = "nomeDeUsuarioInvalido";
        String senha = "senha";
        Cliente cliente = new Cliente(nomeUsuario, senha, 1);
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoCliente.pesquisaPorUsuario(cliente)).thenReturn(null);

        boolean resultadoLogin = daoCliente.login(cliente);
        assertFalse(resultadoLogin);
    }

    @Test
    public void testLoginFalhaSenhaInvalida() throws SQLException {
        String nomeUsuario = "joaosilva";
        String senhaInvalida = "senha_invalida";
        Cliente cliente = new Cliente(nomeUsuario, senhaInvalida, 1);
        Mockito.when(daoCliente.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoCliente.pesquisaPorUsuario(cliente)).thenReturn(cliente);
        Mockito.when(encryptadorMD5Mock.encryptar(senhaInvalida)).thenReturn("senhaCriptografada");

        boolean resultadoLogin = daoCliente.login(cliente);

        assertFalse(resultadoLogin);
    }
}