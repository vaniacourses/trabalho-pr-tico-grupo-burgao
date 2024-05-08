package Helpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import Helpers.EncryptadorMD5;

public class EncryptadorMD5Test {

    @Test
    public void testEncryptar() throws Exception {
        String senha = "senha";

        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5();

        String resultado = encryptadorMD5.encryptar(senha);
        String resultado2 = encryptadorMD5.encryptar(senha);

        // Verifica se o resultado é o esperado
        assertEquals(resultado2, resultado);
        assertEquals(32, resultado.length());
    }

    @Test(expected = ExecutionException.class)
    public void testEncryptarComException() throws Exception {
        // Mock da classe MessageDigest que lança uma exceção
        MessageDigest messageDigest = mock(MessageDigest.class);
        when(messageDigest.digest(any(byte[].class))).thenThrow(Exception.class); // Simulação de exceção

        // Criação do objeto EncryptadorMD5 passando o mock da classe MessageDigest
        EncryptadorMD5 encryptadorMD5 = new EncryptadorMD5(messageDigest);

        // Chama o método encryptar com uma senha de exemplo
        encryptadorMD5.encryptar("senha");
    }
}
