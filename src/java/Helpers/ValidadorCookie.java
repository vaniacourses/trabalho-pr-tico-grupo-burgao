/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import DAO.DaoToken;
import javax.servlet.http.Cookie;

/**
 *
 * @author kener_000
 */

// Classe teve que ser modificada para que fosse possível a realização de testes unitários.
// A dependência do DaoToken foi injetada no construtor da classe.
// Um novo construtor padrão foi criado para que a classe pudesse ser instanciada sem a necessidade de passar o DaoToken como parâmetro.
public class ValidadorCookie {

    private static final String TOKEN = "token";
    private static final String TOKENFUNC = "tokenFuncionario";

    private DaoToken tokenDAO;

    public ValidadorCookie(DaoToken tokenDAO){
        this.tokenDAO = tokenDAO;
    }

    public ValidadorCookie(){
        this.tokenDAO = new DaoToken();
    }
    
    public boolean validar(Cookie[] cookies){
        
        boolean resultado = false;
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals(TOKEN)){
                resultado = tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
    
        public boolean validarFuncionario(Cookie[] cookies){
        
        boolean resultado = false;
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals(TOKENFUNC)){
                resultado = tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
        
    public void deletar(Cookie[] cookies){
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            try{
                if(name.equals(TOKENFUNC)||name.equals(TOKEN)){
                    tokenDAO.remover(value);
                }
            }catch(Exception e){
            throw new RuntimeException(e);
            }
        }
    }
    
    public String getCookieIdCliente(Cookie[] cookies){
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals(TOKEN)){
                String[] palavras;
                palavras = value.split("-");
                return palavras[0];
            }
        }
        return "erro";
    }
    
    public String getCookieIdFuncionario(Cookie[] cookies){
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals(TOKENFUNC)){
                String[] palavras;
                palavras = value.split("-");
                return palavras[0];
            }
        }
        return "erro";
    }
}
