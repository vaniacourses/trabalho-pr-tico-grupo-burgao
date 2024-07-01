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

    @Test
    public void testAcessarCardapio() {
        WebElement nextButton = driver.findElement(new By.ByTagName("button"));
        nextButton.click();

        assertTrue(driver.getCurrentUrl().contains("menu/menu"));
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
