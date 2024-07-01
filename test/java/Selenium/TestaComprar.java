package Selenium;

import Model.Lanche;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestaComprar {

    protected WebDriver driver;

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
        Wait<WebDriver> wait =
                new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(2))
                        .pollingEvery(Duration.ofMillis(300))
                        .ignoring(NoSuchElementException.class);
        Alert alert;

        // testando caessar cardapio
        WebElement nextButton = driver.findElement(new By.ByTagName("button"));
        nextButton.click();
        assertTrue(driver.getTitle().equals("Cardápio"));

        //Testando login
        driver.findElement(By.linkText("Meu Carrinho")).click();
        WebElement emailInput = driver.findElement(By.id("loginInput"));
        WebElement senhaInput = driver.findElement(By.id("senhaInput"));
        emailInput.sendKeys("thyago");
        senhaInput.sendKeys("123456");
        driver.findElement(By.className("buttonSubmit")).click();
        assertTrue(driver.getTitle().equals("Carrinho"));

        //Testando adicionar dois lanches x-salada e x-men
        driver.navigate().to("http://localhost:8080/QT_2024_1_Burgao_war_exploded/view/menu/menu.html");

        Double valor_total = 0.0;

        wait.until(d -> driver.findElement(By.className("divLanche")).isDisplayed());

        List<WebElement> lanches = driver.findElements(By.className("divLanche"));

        for (WebElement lanche : lanches) {
            var titulo = lanche.findElement(By.className("tituloLanche")).getText();
            if (titulo.contains("X-Salada") || titulo.contains("X-Men")) {
                String preco = lanche.findElement(By.className("preco")).getText().split(" ")[1];
                valor_total += Double.parseDouble(preco);
                lanche.findElements(By.tagName("button")).get(1).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                assertTrue(alert.getText().contains("Lanche salvo!"));
                alert.accept();
            }
        }

        assertEquals(25.32 + 26.32, valor_total);
        driver.findElement(By.linkText("Meu Carrinho")).click();
        wait.until(d -> driver.findElement(By.className("buttonSubmitSalvar")));
        driver.findElement(By.className("buttonSubmitSalvar")).click();
        driver.findElement(By.className("buttonSubmit")).click();
        alert = wait.until(ExpectedConditions.alertIsPresent());
        assertTrue(alert.getText().contains("Pedido realizado com sucesso!"));
        alert.accept();
    }

}
