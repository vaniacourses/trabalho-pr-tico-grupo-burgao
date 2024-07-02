package Controllers;

import DAO.DaoCliente;
import Helpers.EncryptadorMD5;
import Model.Cliente;
import Model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegracaoCadastroLogin {
    private DaoCliente daoCliente;
    EncryptadorMD5 encryptadorMD5;

    @BeforeEach
    void setUp() {
        daoCliente = new DaoCliente();
        encryptadorMD5 = new EncryptadorMD5();
    }

    @Test
    public void integracaoLoginEncryptador()  {
        Cliente cliente = new Cliente();
        cliente.setNome("Daniel");
        cliente.setSobrenome("Garrido");
        cliente.setTelefone("11999999999");
        cliente.setUsuario("daniel");
        cliente.setSenha("123456");
        Endereco endereco = new Endereco();
        endereco.setRua("Rua dos Bobos");
        endereco.setNumero(0);
        endereco.setBairro("Bairro do Limoeiro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        cliente.setEndereco(endereco);
        cadastraCliente(cliente);
        assertTrue(daoCliente.login(cliente));
    }

    private void cadastraCliente(Cliente cliente) {
        daoCliente.salvar(cliente);
    }
}
