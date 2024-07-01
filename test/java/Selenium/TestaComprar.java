package Selenium;

import Model.Lanche;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestaComprar {

    private WebDriver driver;

    private By meuCarrinhoBotao = By.linkText("Meu Carrinho");



    @BeforeAll
    public static void configuraDriver() {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Webdrivers\\geckodriver.exe");
    }

    @BeforeEach
    public void createDriver() {
        driver = new FirefoxDriver();
        driver.get("http://localhost:8080/QT_2024_1_Burgao_war_exploded/");
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testComprarLanches() {
        // testando acessar cardapio
        WebElement nextButton = driver.findElement(new By.ByTagName("button"));
        nextButton.click();
        CardapioPage cardapioPage = new CardapioPage(driver);
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
