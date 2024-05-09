package Helpers;

import Helpers.EncryptadorMD5;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EncryptadorMD5Test {

    @Test
    public void testEncryptar() {
        String senha = "senha";

        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5();

        String resultado = encryptadorMD5.encryptar(senha);
        String resultado2 = encryptadorMD5.encryptar(senha);

        // Verifica se o resultado é o esperado
        assertEquals(resultado2, resultado);
        assertEquals(32, resultado.length());
    }
}
