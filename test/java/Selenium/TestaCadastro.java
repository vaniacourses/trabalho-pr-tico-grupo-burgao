package Selenium;

import Model.Cliente;
import Model.Endereco;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestaCadastro extends TestaBase {

    @Test
    public void testaCadastro() {
        CardapioPage cardapioPage = navigateToCardapio();
        cardapioPage.acessarCarrinho();
        LoginPage loginPage = new LoginPage(driver);
        CadastroPage cadastroPage = loginPage.navigateToCadastro();

        Cliente cliente = new Cliente();
        cliente.setNome("Daniel");
        cliente.setSobrenome("Garrido");
        cliente.setTelefone("11999999999");
        cliente.setUsuario("daniel");
        cliente.setSenha("123456");
        Endereco endereco = new Endereco();
        endereco.setRua("Rua dos Bobos");
        endereco.setNumero(0);
        endereco.setBairro("Bairro do Limoeiro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        cliente.setEndereco(endereco);

        assertTrue(cadastroPage.cadastraCliente(cliente));
    }
}
