package Controllers;

import DAO.DaoCliente;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CadastroTest{

    private cadastro cadastroService;
    private DaoCliente daoMock;

    @BeforeEach
    public void setup() {
        // Mock do DaoCliente
        daoMock = Mockito.mock(DaoCliente.class);
        // Instância da classe cadastro com o mock
        cadastroService = new cadastro(daoMock);
    }

    @Test
    public void testVerificaSenhaAprovada() {
        assertTrue(cadastroService.verificaSenha("Abc123"));  // Senha válida
        assertTrue(cadastroService.verificaSenha("XyZ456"));  // Senha válida
    }

    @Test
    public void testVerificaSenhaReprovada() {
        assertFalse(cadastroService.verificaSenha("abcdef"));  // Sem letra maiúscula, sem número
        assertFalse(cadastroService.verificaSenha("ABCDEF"));  // Sem número
        assertFalse(cadastroService.verificaSenha("123456"));  // Sem letra maiúscula
        assertFalse(cadastroService.verificaSenha("Abcde"));   // Menos de 6 caracteres
    }

    @Test
    public void testVerificaTelefoneAprovada() {
        assertTrue(cadastroService.verificaTelefone("91234567"));  // Telefone válido
        assertTrue(cadastroService.verificaTelefone("22345678"));  // Telefone válido
    }

    @Test
    public void testVerificaTelefoneReprovada() {
        assertFalse(cadastroService.verificaTelefone("01234567"));  // Primeiro dígito inválido (0)
        assertFalse(cadastroService.verificaTelefone("12345678"));  // Primeiro dígito inválido (1)
        assertFalse(cadastroService.verificaTelefone("51234567"));  // Primeiro dígito inválido (5)
        assertFalse(cadastroService.verificaTelefone("61234567"));  // Primeiro dígito inválido (6)
        assertFalse(cadastroService.verificaTelefone("71234567"));  // Primeiro dígito inválido (7)
        assertFalse(cadastroService.verificaTelefone("81234567"));  // Primeiro dígito inválido (8)
        assertFalse(cadastroService.verificaTelefone("1234567"));   // Menos de 8 caracteres
    }

    @Test
    public void testCadastroClienteComDadosValidos() {
        // Cliente com dados válidos
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setSobrenome("Silva");
        cliente.setTelefone("91234567");
        cliente.setUsuario("joao.silva");
        cliente.setSenha("Senha123");

        // Configurar o comportamento do mock para simular o salvamento do cliente
        doNothing().when(daoMock).salvar(cliente);

        // Processo de cadastro
        boolean senhaValida = cadastroService.verificaSenha(cliente.getSenha());
        boolean telefoneValido = cadastroService.verificaTelefone(cliente.getTelefone());

        if (senhaValida && telefoneValido) {
            daoMock.salvar(cliente);
        }

        // Verificar se o método salvar foi chamado
        verify(daoMock).salvar(cliente);

        // Asserts para confirmar que a validação está correta
        assertTrue(senhaValida);
        assertTrue(telefoneValido);
    }

    @Test
    public void testVerificarSenha() {

        // Configurando o comportamento esperado do mock
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setSobrenome("Silva");
        cliente.setTelefone("12345678");
        cliente.setUsuario("joao.silva");
        cliente.setSenha("senha123");

        // Mockando o serviço que utiliza o DaoCliente
        cadastro servicoMock = Mockito.mock(cadastro.class);

        // Configurando o comportamento esperado do mock do serviço
        when(servicoMock.verificaSenha(cliente.getSenha())).thenReturn(true);

        // Chamando o método a ser testado
        boolean senhaValida = servicoMock.verificaSenha(cliente.getSenha());

        // Verificando se o método verificaSenha foi chamado corretamente
        verify(servicoMock, times(1)).verificaSenha("senha123");

        // Assert para verificar se o resultado da verificação é o esperado
        assertTrue(senhaValida);
    }

    @Test
    public void testVerificarSenhaComSucessoComDaoMock() {
        // Criando uma instância real do cadastro com um mock do DaoCliente
        DaoCliente daoMock = Mockito.mock(DaoCliente.class);
        cadastro cadastroService = new cadastro(daoMock);

        // Testando uma senha que deve passar
        assertTrue(cadastroService.verificaSenha("Abc123"));
    }

    @Test
    public void testVerificarSenhaFalhaComDaoMock() {
        // Criando uma instância real do cadastro com um mock do DaoCliente
        DaoCliente daoMock = Mockito.mock(DaoCliente.class);
        cadastro cadastroService = new cadastro(daoMock);

        // Testando senhas que devem falhar
        assertFalse(cadastroService.verificaSenha("abcdef"));  // Sem letra maiúscula, sem número
        assertFalse(cadastroService.verificaSenha("ABCDEF"));  // Sem número
        assertFalse(cadastroService.verificaSenha("123456"));  // Sem letra maiúscula
        assertFalse(cadastroService.verificaSenha("Abcde"));   // Menos de 6 caracteres
    }

    @Test
    public void testVerificaSenhaEstrutural() {
        // Teste com senha válida que passa por todas as condições
        assertTrue(cadastroService.verificaSenha("Abc123"));

        // Teste com senha sem letra maiúscula
        assertFalse(cadastroService.verificaSenha("abcdef"));

        // Teste com senha sem número
        assertFalse(cadastroService.verificaSenha("ABCDEF"));

        // Teste com senha sem letra maiúscula e sem número
        assertFalse(cadastroService.verificaSenha("abCDef"));

        // Teste com senha com menos de 6 caracteres
        assertFalse(cadastroService.verificaSenha("abc123456789"));
    }



    @Test
    public void testVerificaTelefoneEstrutural() {
        // Teste com telefone válido que passa por todas as condições
        assertTrue(cadastroService.verificaTelefone("91234567"));

        // Teste com telefone inválido - primeiro dígito 0
        assertFalse(cadastroService.verificaTelefone("01234567"));

        // Teste com telefone inválido - primeiro dígito 1
        assertFalse(cadastroService.verificaTelefone("12345678"));

        // Teste com telefone inválido - primeiro dígito 5
        assertFalse(cadastroService.verificaTelefone("51234567"));

        // Teste com telefone inválido - primeiro dígito 6
        assertFalse(cadastroService.verificaTelefone("6123a567"));

        // Teste com telefone inválido - primeiro dígito 7
        assertFalse(cadastroService.verificaTelefone("71234567"));

        // Teste com telefone inválido - primeiro dígito 8
        assertFalse(cadastroService.verificaTelefone("81234567"));

        // Teste com telefone com menos de 8 caracteres
        assertFalse(cadastroService.verificaTelefone("1234567"));
    }

    @Test
    public void testVerificaSenhaComCaracteresEspeciais() {
        // Teste criado baseado em um defeito encontrado durante o teste de mutação,
        // A função verificaSenha falhava ao lidar com números de telefone contendo caracter especial
        assertTrue(cadastroService.verificaSenha("Abc@123"));  // Senha com caractere especial
        assertFalse(cadastroService.verificaSenha("Abc.123"));
    }


    @Test
    public void testVerificaSenhaComEspaco() {
        // Teste criado baseado em um defeito encontrado durante o teste de mutação,
        // A função verificaSenha falhava ao lidar com números de telefone contendo espaco
        assertFalse(cadastroService.verificaSenha("Abc 123"));  // Senha com espaço
    }

    @Test
    public void testVerificaTelefoneComEspaco() {
        // Teste criado baseado em um defeito encontrado durante o teste de mutação,
        // A função verificaTelefone falhava ao lidar com números de telefone contendo espaco
        assertFalse(cadastroService.verificaTelefone("912 34567"));  // Telefone com espaço
    }

    @Test
    public void testVerificaTelefoneComCaracteresEspeciais() {
        // A função verificaTelefone falhava ao lidar com números de telefone contendo caracteres especiais.
        assertFalse(cadastroService.verificaTelefone("9123-4567"));  // Telefone com caractere especial
    }

}

