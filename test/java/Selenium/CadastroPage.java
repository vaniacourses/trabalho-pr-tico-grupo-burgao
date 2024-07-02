package Selenium;

import Model.Cliente;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.lang.Thread.sleep;

public class CadastroPage extends BasePage {

    @FindBy(name = "nome")
    private WebElement nomeInput;
    @FindBy(name = "sobrenome")
    private WebElement sobrenomeInput;
    @FindBy(name = "telefone")
    private WebElement telefoneInput;
    @FindBy(name = "usuario")
    private WebElement usuarioInput;
    @FindBy(name = "senha")
    private WebElement senhaInput;
    @FindBy(name = "rua")
    private WebElement ruaInput;
    @FindBy(name = "numero")
    private WebElement numeroInput;
    @FindBy(name = "bairro")
    private WebElement bairroInput;
    @FindBy(name = "cidade")
    private WebElement cidadeInput;
    @FindBy(id = "UF")
    private WebElement estadoInput;
    @FindBy(className = "buttonSubmit")
    private WebElement submitButton;

    public CadastroPage(WebDriver driver) {
        super(driver);
        if (!driver.getTitle().equals("Cadastro")) {
            throw new IllegalStateException("Está não é a pagina de cadastro," +
                    "página atual é" + driver.getCurrentUrl());
        }
        PageFactory.initElements(driver, this);
    }

    public boolean cadastraCliente(Cliente cliente) {
        nomeInput.sendKeys(cliente.getNome());
        sobrenomeInput.sendKeys(cliente.getSobrenome());
        telefoneInput.sendKeys(cliente.getTelefone());
        usuarioInput.sendKeys(cliente.getUsuario());
        senhaInput.sendKeys(cliente.getSenha());
        ruaInput.sendKeys(cliente.getEndereco().getRua());
        numeroInput.sendKeys(Integer
                        .toString(cliente.getEndereco().getNumero()));
        bairroInput.sendKeys(cliente.getEndereco().getBairro());
        cidadeInput.sendKeys(cliente.getEndereco().getCidade());
        estadoInput.sendKeys(cliente.getEndereco().getEstado());
        submitButton.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        return alert.getText().contains("Usuário Cadastrado!");
    }
}
