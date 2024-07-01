package Selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestaComprar extends TestaBase {

    @Test
    public void testComprarLanches() {
        CardapioPage cardapioPage = navigateToCardapio();
        cardapioPage.acessarCarrinho();

        //Testando login
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.loginValidUser("thyago", "123456"));
        cardapioPage.navigateToCardapio();

        //Testando adicionar dois lanches x-salada e x-men
        assertTrue(cardapioPage.adicionaLancheAoCarrinho("X-Salada"));
        assertTrue(cardapioPage.adicionaLancheAoCarrinho("X-Men"));
        cardapioPage.acessarCarrinho();

        // Testando finalizar compra
        MeuCarrinhoPage meuCarrinhoPage = new MeuCarrinhoPage(driver);
        // preço do x-salada + x-men
        Double valor_total_esperado = 25.32 + 26.32;
        assertEquals(valor_total_esperado, meuCarrinhoPage.getValorTotal());
        assertTrue(meuCarrinhoPage.finalizarCompra());
    }
}
