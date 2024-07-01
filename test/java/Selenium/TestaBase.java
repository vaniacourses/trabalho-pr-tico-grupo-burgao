package Selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestaBase {
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

    protected CardapioPage navigateToCardapio() {
        WebElement nextButton = driver.findElement(new By.ByTagName("button"));
        nextButton.click();
        return new CardapioPage(driver);
    }
}
