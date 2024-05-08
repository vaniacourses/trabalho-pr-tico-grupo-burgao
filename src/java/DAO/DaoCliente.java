/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helpers.EncryptadorMD5;
import Model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
/**
 *
 * @author kener_000
 */
public class DaoCliente {

    private Connection conecta;

    private static final Logger logger = Logger.getLogger(DaoCliente.class.getName());
    public static final String COL_ID_CLIENTE = "id_cliente";
    public static final String COL_NOME = "nome";
    public static final String COL_SOBRENOME = "sobrenome";
    public static final String COL_TELEFONE = "telefone";
    public static final String COL_USUARIO = "usuario";
    public static final String COL_SENHA = "senha";

    public DaoCliente(){
        this.conecta = new DaoUtil().conecta();
    }

    public void salvar(Cliente cliente){

        String sql = "INSERT INTO tb_clientes(nome, sobrenome, telefone, usuario, senha, fg_ativo, id_endereco) "
                + "VALUES(?,?,?,?, MD5(?),?,?)";


        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getUsuario());
            stmt.setString(5, cliente.getSenha());
            stmt.setInt(6, cliente.getFg_ativo());
            DaoEndereco dend = new DaoEndereco();
            if(dend.validaEndereco(cliente.getEndereco()) == 0){
                dend.salvar(cliente.getEndereco());
                stmt.setInt(7, dend.validaEndereco(cliente.getEndereco()));
            } else{
                stmt.setInt(7, dend.validaEndereco(cliente.getEndereco()));
            }
            stmt.execute();
            stmt.close();


        }catch (SQLException e) {
            throw new SQLException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listarTodos(){
        String sql = "SELECT " + COL_ID_CLIENTE + ", " + COL_NOME + ", " + COL_SOBRENOME + ", " + COL_TELEFONE +
                ", " + COL_USUARIO + " " + "FROM tb_clientes WHERE fg_Ativo='1' ORDER BY " + COL_ID_CLIENTE;
        ResultSet rs;
        List<Cliente> clientes = new ArrayList<>();

        try{

            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()){

                Cliente cliente = new Cliente();
                cliente.setId_cliente(rs.getInt(COL_ID_CLIENTE));
                cliente.setNome(rs.getString(COL_NOME));
                cliente.setSobrenome(rs.getString(COL_SOBRENOME));
                cliente.setTelefone(rs.getString(COL_TELEFONE));
                cliente.setUsuario(rs.getString(COL_USUARIO));
                cliente.setSenha(rs.getString(COL_SENHA));
                cliente.setFg_ativo(1);

                clientes.add(cliente);
            }
            rs.close();
            stmt.close();
            return clientes;


        } catch (SQLException e) {
            throw new SQLException("Erro ao listar clientes: " + e.getMessage());
        }

    }

    public Cliente pesquisaPorUsuario(Cliente cliente){
        String sql = "SELECT * FROM tb_clientes WHERE usuario='"+cliente.getUsuario()+"'";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();

        try{

            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()){

                clienteResultado.setId_cliente(rs.getInt(COL_ID_CLIENTE));
                clienteResultado.setNome(rs.getString(COL_NOME));
                clienteResultado.setSobrenome(rs.getString(COL_SOBRENOME));
                clienteResultado.setTelefone(rs.getString(COL_TELEFONE));
                clienteResultado.setUsuario(rs.getString(COL_USUARIO));
                clienteResultado.setSenha(rs.getString(COL_SENHA));
                clienteResultado.setFg_ativo(1);

            }
            rs.close();
            stmt.close();
            return clienteResultado;


        } catch (SQLException e) {
            throw new SQLException("Erro ao pesquisar por usuário: " + e.getMessage());
        }

    }

    public Cliente pesquisaPorID(String clienteId){
        String sql = "SELECT * FROM tb_clientes WHERE id_cliente='"+clienteId+"'";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();

        try{

            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()){
                clienteResultado.setId_cliente(rs.getInt(COL_ID_CLIENTE));
                clienteResultado.setNome(rs.getString(COL_NOME));
                clienteResultado.setSobrenome(rs.getString(COL_SOBRENOME));
                clienteResultado.setTelefone(rs.getString(COL_TELEFONE));
                clienteResultado.setFg_ativo(1);
            }
            rs.close();
            stmt.close();
            return clienteResultado;


        } catch (SQLException e) {
            throw new SQLException("Erro ao pesquisar por ID: " + e.getMessage());
        }

    }

    public boolean login(Cliente cliente){
        String sql = "SELECT usuario, senha, fg_ativo FROM tb_clientes WHERE usuario = ?";

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getUsuario());

            ResultSet rs;
            rs = stmt.executeQuery();
            Cliente validCliente = new Cliente();
            EncryptadorMD5 md5 = new EncryptadorMD5();

            while (rs.next()){
                validCliente.setUsuario(rs.getString(COL_USUARIO));
                validCliente.setSenha(rs.getString(COL_SENHA));
                validCliente.setFg_ativo(rs.getInt("fg_ativo"));
            }

            rs.close();
            stmt.close();

            if ((md5.encryptar(cliente.getSenha()).equals(validCliente.getSenha())
                    && (validCliente.getFg_ativo() == 1))) {
                logger.info("Login efetuado com sucesso!");
                return true;
            } else {
                logger.warning("Falha no login: Usuário ou senha inválidos.");
                return false;
            }

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }

        return false;
    }

}


