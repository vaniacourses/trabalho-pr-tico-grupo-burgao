package Selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestaLogout extends TestaBase {

    @Test
    public void testLogout() {
        CardapioPage cardapioPage = navigateToCardapio();
        cardapioPage.acessarCarrinho();
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.loginValidUser("thyago", "123456"));
        MeuCarrinhoPage meuCarrinhoPage = new MeuCarrinhoPage(driver);
        assertTrue(meuCarrinhoPage.logout());
    }
}