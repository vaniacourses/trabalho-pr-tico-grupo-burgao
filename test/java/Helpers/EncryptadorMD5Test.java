package Helpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import Helpers.EncryptadorMD5;

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
