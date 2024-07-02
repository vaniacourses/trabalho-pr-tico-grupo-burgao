package Helpers;

import DAO.DaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidadorCookieTest {


    private ValidadorCookie validadorCookie;
    private DaoToken mockDaoToken;


    @BeforeEach
    void inicializa() {
        mockDaoToken = mock(DaoToken.class);
        validadorCookie = new ValidadorCookie(mockDaoToken);
    }

    @Test
    void validarTokenValido() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "TokenValido")};
        when(mockDaoToken.validar("TokenValido")).thenReturn(true);
        assertTrue(validadorCookie.validar(cookies));
    }

    @Test
    void validarTokenInvalido() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "TokenInvalido")};
        when(mockDaoToken.validar("TokenInvalido")).thenReturn(false);
        assertFalse(validadorCookie.validar(cookies));
    }

    @Test
    void validarSemToken() {
        Cookie[] cookies = new Cookie[]{new Cookie("outroCookie", "true")};
        assertFalse(validadorCookie.validar(cookies));
    }

    @Test
    void validarCookiesNull() {
        assertThrows(NullPointerException.class, () -> validadorCookie.validar(null));
    }


    @Test
    void validarFuncionarioTokenValido() {
        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "TokenValido")};
        when(mockDaoToken.validar("TokenValido")).thenReturn(true);
        assertTrue(validadorCookie.validarFuncionario(cookies));
    }

    @Test
    void validarFuncionarioTokenInvaldo() {
        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "TokenInvalido")};
        when(mockDaoToken.validar("TokenInvalido")).thenReturn(false);
        assertFalse(validadorCookie.validarFuncionario(cookies));
    }

    @Test
    void validarFuncionarioSemToken() {
        Cookie[] cookies = new Cookie[]{new Cookie("outroCookie", "true")};
        assertFalse(validadorCookie.validarFuncionario(cookies));
    }

    @Test
    void validarFuncionarioCookiesNull() {
        assertThrows(NullPointerException.class, () -> validadorCookie.validar(null));
    }


    @Test
    void getCookieIdClienteTest() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "123-4")};
        assertEquals("123", validadorCookie.getCookieIdCliente(cookies));
    }

    @Test
    void getCookieIdFuncionarioTest() {
        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "123-456")};
        assertEquals("123", validadorCookie.getCookieIdFuncionario(cookies));
    }

    @Test
    void deletarTest() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "tokenValido")};
        doNothing().when(mockDaoToken).remover("tokenValido");
        validadorCookie.deletar(cookies);
        verify(mockDaoToken, times(1)).remover("tokenValido");
    }

    @Test
    void deletarAmbosTest() {
        Cookie[] cookies = new Cookie[]{
                new Cookie("token", "tokenValido"),
                new Cookie("tokenFuncionario", "tokenValido")
        };
        doNothing().when(mockDaoToken).remover("tokenValido");
        validadorCookie.deletar(cookies);
        verify(mockDaoToken, times(2)).remover("tokenValido");
    }

    @Test
    void deletarNadaTest() {
        Cookie[] cookies = new Cookie[]{new Cookie("outroCookie", "valorQualquer")};
        validadorCookie.deletar(cookies);
        verify(mockDaoToken, times(0)).remover(anyString());
    }

    @Test
    void deletarComErro() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "tokenValido")};
        doThrow(new RuntimeException()).when(mockDaoToken).remover("tokenValido");
        assertThrows(RuntimeException.class, () -> validadorCookie.deletar(cookies));
    }
}


