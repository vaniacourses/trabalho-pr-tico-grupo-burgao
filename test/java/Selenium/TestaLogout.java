package Selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestaLogout {
    private WebDriver driver;

    @BeforeAll
    public static void configuraDriver() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Daniel\\Downloads\\geckodriver.exe");
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
    public void testLogout() {
        WebElement nextButton = driver.findElement(new By.ByTagName("button"));
        nextButton.click();
        CardapioPage cardapioPage = new CardapioPage(driver);
        cardapioPage.acessarCarrinho();
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.loginValidUser("thyago", "123456"));
        MeuCarrinhoPage meuCarrinhoPage = new MeuCarrinhoPage(driver);
        assertTrue(meuCarrinhoPage.logout());
    }
}