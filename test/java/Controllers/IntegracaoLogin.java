package Controllers;

import DAO.DaoCliente;
import Helpers.EncryptadorMD5;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegracaoLogin {
    private DaoCliente daoCliente;
    EncryptadorMD5 encryptadorMD5;

    @BeforeEach
    void setUp() {
        daoCliente = new DaoCliente();
        encryptadorMD5 = new EncryptadorMD5();
    }

    @Test
    public void integracaoLoginEncryptador()  {
        Cliente thyago = new Cliente();
        thyago.setUsuario("thyago");
        thyago.setSenha("123456");
        thyago.setFg_ativo(1);
        thyago.setId_cliente(2);
        assertTrue(daoCliente.login(thyago));
    }
}
