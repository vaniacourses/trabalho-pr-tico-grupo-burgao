package Helpers;

import DAO.DaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ValidadorCookieTest {

    private ValidadorCookie validadorCookie;
    private DaoToken mockDaoToken;


    @BeforeEach
    void inicializa() {
        mockDaoToken = Mockito.mock(DaoToken.class);
        validadorCookie = new ValidadorCookie(mockDaoToken);
    }

    @Test
    void retornaTrueQuandoTokenValido() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "validToken")};
        when(mockDaoToken.validar("validToken")).thenReturn(true);
        assertTrue(validadorCookie.validar(cookies));
    }

    @Test
    void retornaFalseQuandoTokenInvalido() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "invalidToken")};
        when(mockDaoToken.validar("invalidToken")).thenReturn(false);
        assertFalse(validadorCookie.validar(cookies));
    }

    @Test
    void shouldReturnFalseWhenNoTokenCookiePresent() {
        Cookie[] cookies = new Cookie[]{new Cookie("otherCookie", "true")};
        assertFalse(validadorCookie.validar(cookies));
    }

    @Test
    void shouldReturnFalseWhenCookiesAreNull() {
        assertFalse(validadorCookie.validar(null));
    }
}