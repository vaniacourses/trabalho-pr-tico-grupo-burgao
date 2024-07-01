package Selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CardapioPage extends BasePage {

    @FindBy(linkText = "Meu Carrinho")
    private WebElement meuCarrinhoBotao;

    private By divLanche = By.className("divLanche");

    public CardapioPage(WebDriver driver) {
        super(driver);
        if (!driver.getTitle().equals("Cardápio")) {
            throw new IllegalStateException("Está não é a pagina de cardápio," +
                    "página atual é" + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public void acessarCarrinho() {
        meuCarrinhoBotao.click();
    }

    public void navigateToCardapio() {
        driver.navigate().to("http://localhost:8080/QT_2024_1_Burgao_war_exploded/view/menu/menu.html");
    }

    public boolean adicionaLancheAoCarrinho(String nomeLanche) {
        wait.until(d -> driver.findElement(divLanche).isDisplayed());
        List<WebElement> lanches = driver.findElements(divLanche);

        for (WebElement lanche : lanches) {
            var titulo = lanche.findElement(By.className("tituloLanche")).getText();
            if (titulo.contains(nomeLanche)) {
                lanche.findElements(By.tagName("button")).get(1).click();
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());

                if (alert.getText().contains("Lanche salvo!")) {
                    alert.accept();
                    return true;
                }
            }
        }
        return false;
    }
}
