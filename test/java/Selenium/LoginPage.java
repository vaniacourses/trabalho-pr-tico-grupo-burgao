package Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {

    @FindBy(id = "loginInput")
    private WebElement loginInput;
    @FindBy(id = "senhaInput")
    private WebElement senhaInput;
    @FindBy(className = "buttonSubmit")
    private WebElement submitButton;

    public LoginPage(WebDriver driver){
        super(driver);
        if (!driver.getTitle().equals("Login")) {
            throw new IllegalStateException("Está não é a pagina de login," +
                    "página atual é" + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public boolean loginValidUser(String userName, String password) {
        loginInput.sendKeys(userName);
        senhaInput.sendKeys(password);
        submitButton.click();
        wait.until(d -> driver.findElement(By.className("nomeCliente")).isDisplayed());
        return driver.getTitle().equals("Carrinho");
    }

    public CadastroPage navigateToCadastro() {
        driver.findElement(By.linkText("Primera Vez aqui? Crie uma nova conta!")).click();
        return new CadastroPage(driver);
    }
}
