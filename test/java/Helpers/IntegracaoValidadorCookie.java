package Helpers;

import DAO.DaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;

class IntegracaoValidadorCookie {

    private ValidadorCookie validadorCookie;
    private DaoToken daoToken;

    @BeforeEach
    void setUp() {
        daoToken = new DaoToken();
        validadorCookie = new ValidadorCookie(daoToken);
    }

    @Test
    void testaTokenValido() {
        // Insere token válido no banco de dados
        daoToken.salvar("validToken");

        Cookie[] cookies = new Cookie[]{new Cookie("token", "validToken")};
        assertTrue(validadorCookie.validar(cookies));

        // Apaga o token do teste do banco de dados após o teste
        daoToken.remover("validToken");
    }

    @Test
    void testValidarComTokenInvalido() {
        Cookie[] cookies = new Cookie[]{new Cookie("token", "invalidToken")};
        assertFalse(validadorCookie.validar(cookies));
    }

    @Test
    void testValidarFuncionarioComTokenValido() {
        // Insere token válido no banco de dados
        daoToken.salvar("validTokenFunc");

        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "validTokenFunc")};
        assertTrue(validadorCookie.validarFuncionario(cookies));

        // Limpa o banco de dados após o teste
        daoToken.remover("validTokenFunc");
    }

    @Test
    void testValidarFuncionarioComTokenInvalido() {
        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "invalidToken")};
        assertFalse(validadorCookie.validarFuncionario(cookies));
    }

    @Test
    void testDeletarComTokenValido() {
        // Insere token válido no banco de dados
        daoToken.salvar("validToken");

        Cookie[] cookies = new Cookie[]{new Cookie("token", "validToken")};
        validadorCookie.deletar(cookies);

        // Verifica se o token foi removido
        assertFalse(daoToken.validar("validToken"));
    }

    @Test
    void testGetCookieIdCliente() {
        // Insere token válido no banco de dados
        daoToken.salvar("123-validToken");

        Cookie[] cookies = new Cookie[]{new Cookie("token", "123-validToken")};
        assertEquals("123", validadorCookie.getCookieIdCliente(cookies));

        // Limpa o banco de dados após o teste
        daoToken.remover("123-validToken");
    }

    @Test
    void testGetCookieIdFuncionario() {
        // Insere token válido no banco de dados
        daoToken.salvar("123-validTokenFunc");

        Cookie[] cookies = new Cookie[]{new Cookie("tokenFuncionario", "123-validTokenFunc")};
        assertEquals("123", validadorCookie.getCookieIdFuncionario(cookies));

        // Limpa o banco de dados após o teste
        daoToken.remover("123-validTokenFunc");
    }

}