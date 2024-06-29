package Helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptadorMD5Test {

    @Test
    public void testEncryptar() {
        String senha = "senha";

        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5();

        String resultado = encryptadorMD5.encryptar(senha);
        String resultado2 = encryptadorMD5.encryptar(senha);

        assertEquals(resultado2, resultado);
        assertEquals(32, resultado.length());
    }

    @Test
    public void testHashesDiferentes() {
        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5();
        String senha1 = "senha";
        String senha2 = "senha2";

        String result1 = encryptadorMD5.encryptar(senha1);
        String result2 = encryptadorMD5.encryptar(senha2);

        assertNotEquals(result1, result2);
    }

    @Test
    public void testRuntimeException() {
        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5();

        assertThrows(RuntimeException.class, () -> encryptadorMD5.encryptar(null));
    }
}
