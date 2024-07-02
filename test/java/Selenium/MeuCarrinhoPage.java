package Selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MeuCarrinhoPage extends BasePage {

    private By botaoFormaDePagamento = By.className("buttonSubmitSalvar");
    private By botaoFinalizarCompra = By.className("buttonSubmit");
    private By valorTotal = By.className("valuePedidos");
    private By botaoLogout = By.className("buttonLogout");

    public MeuCarrinhoPage(WebDriver driver) {
        super(driver);
        wait.until(d -> d.getTitle().equals("Carrinho"));
    }

    public boolean finalizarCompra() {
        wait.until(d -> driver.findElement(botaoFormaDePagamento).isDisplayed());
        driver.findElement(botaoFormaDePagamento).click();
        driver.findElement(botaoFinalizarCompra).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        if (alert.getText().contains("Pedido realizado com sucesso!")) {
            alert.accept();
            return true;
        }
        return false;
    }

    public Double getValorTotal() {
        wait.until(d -> d.findElement(valorTotal).isDisplayed());
        String valor = driver.findElement(valorTotal).getText();
        return Double.parseDouble(valor.split(" ")[1]);
    }

    public boolean logout() {
        wait.until(d -> d.findElement(botaoLogout).isDisplayed());
        driver.findElement(botaoLogout).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        if (alert.getText().contains("Deslogado")) {
            alert.accept();
            return true;
        };

        return false;
    }
}