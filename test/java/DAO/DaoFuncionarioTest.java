import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import DAO.DaoFuncionario;
import Helpers.EncryptadorMD5;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Funcionario;

@ExtendWith(MockitoExtension.class)
public class DaoFuncionarioTest {

    @Mock
    private Connection conexaoMock;

    @Mock
    private EncryptadorMD5 encryptadorMD5Mock;

    @InjectMocks
    private DaoFuncionario daoFuncionario;

    @Test
    public void testCriarFuncionario() {
        Funcionario funcionario = new Funcionario("Fulano", "da Silva", "fulano.silva", "123456", "Gerente", 2500.00, 1, 1);
        assertNotNull(funcionario);
    }

    @Test
    public void testSalvarFuncionario() throws SQLException {
        Funcionario funcionario = new Funcionario("Fulano", "da Silva", "fulano.silva", "123456", "Gerente", 2500.00, 1, 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        daoFuncionario.salvar(funcionario);

        PreparedStatement preparedStatement = Mockito.verify(conexaoMock).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement).setString(1, funcionario.getNome());
        Mockito.verify(preparedStatement).setString(2, funcionario.getSobrenome());
        Mockito.verify(preparedStatement).setString(3, funcionario.getUsuario());
        Mockito.verify(preparedStatement).setString(4, encryptadorMD5Mock.encryptar(funcionario.getSenha()));
        Mockito.verify(preparedStatement).setString(5, funcionario.getCargo());
        Mockito.verify(preparedStatement).setDouble(6, funcionario.getSalario());
        Mockito.verify(preparedStatement).setInt(7, funcionario.getCad_por());
        Mockito.verify(preparedStatement).setInt(8, funcionario.getFg_ativo());
        preparedStatement.executeUpdate();
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testPesquisaPorUsuarioComSucesso() throws SQLException {
        Funcionario funcionario = new Funcionario("fulano.silva", "123456", 1);
        Funcionario funcionarioResultado = new Funcionario("Fulano", "da Silva", "fulano.silva", "senhaCriptografada", "Gerente", 2500.00, 1, 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoFuncionario.pesquisaPorUsuario(funcionario)).thenReturn(funcionarioResultado);
        Mockito.when(encryptadorMD5Mock.encryptar(funcionario.getSenha())).thenReturn("senhaCriptografada");

        Funcionario resultado = daoFuncionario.pesquisaPorUsuario(funcionario);

        assertNotNull(resultado);
        assertEquals(funcionarioResultado, resultado);
    }

    @Test
    public void testPesquisaPorUsuarioInexistente() throws SQLException {
        Funcionario funcionario = new Funcionario("usuario_inexistente", "123456", 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoFuncionario.pesquisaPorUsuario(funcionario)).thenReturn(null);

        Funcionario resultado = daoFuncionario.pesquisaPorUsuario(funcionario);

        assertNull(resultado);
    }

    @Test
    public void testLoginSucesso() throws SQLException {
        String nomeUsuario = "fulano.silva";
        String senha = "123456";
        Funcionario funcionario = new Funcionario(nomeUsuario, null, nomeUsuario, senha, null, 0.0, 0, 1);
        Funcionario funcionarioResultado = new Funcionario("Fulano", "da Silva", "fulano.silva", "senhaCriptografada", "Gerente", 2500.00, 1, 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoFuncionario.pesquisaPorUsuario(funcionario)).thenReturn(funcionarioResultado);
        Mockito.when(encryptadorMD5Mock.encryptar(funcionario.getSenha())).thenReturn("senhaCriptografada");

        boolean autenticado = daoFuncionario.login(funcionario);

        assertTrue(autenticado);
    }

    @Test
    public void testLoginFalhaUsuarioInvalido() throws SQLException {
        String nomeUsuario = "usuario_invalido";
        String senha = "123456";
        Funcionario funcionario = new Funcionario(nomeUsuario, null, nomeUsuario, senha, null, 0.0, 0, 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoFuncionario.pesquisaPorUsuario(funcionario)).thenReturn(null);

        boolean autenticado = daoFuncionario.login(funcionario);

        assertFalse(autenticado);
    }

    @Test
    public void testLoginFalhaSenhaInvalida() throws SQLException {
        String nomeUsuario = "fulano.silva";
        String senha = "senha_invalida";
        Funcionario funcionario = new Funcionario(nomeUsuario, null, nomeUsuario, senha, null, 0.0, 0, 1);
        Funcionario funcionarioResultado = new Funcionario("Fulano", "da Silva", "fulano.silva", "senhaCriptografada", "Gerente", 2500.00, 1, 1);
        Mockito.when(daoFuncionario.conecta()).thenReturn(conexaoMock);
        Mockito.when(daoFuncionario.pesquisaPorUsuario(funcionario)).thenReturn(funcionarioResultado);
        Mockito.when(encryptadorMD5Mock.encryptar(funcionario.getSenha())).thenReturn("senhaCriptografada");

        boolean autenticado = daoFuncionario.login(funcionario);

        assertFalse(autenticado);
    }
}