
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    
    private String con_banco;
    private String usuario_mysql;
    private String senha_mysql;
    private Connection conn;
    private String banco = con_banco = "jdbc:mysql://127.0.0.1:3306/ltproject?useSSL=false";
    
    private static Conexao conexao;
            
    public static Conexao getInstance(){
        
        if(conexao == null) {
            conexao = new Conexao();
        }
        else try {
            if(conexao.getConexao().isClosed()){
                conexao = new Conexao();
            }
        } catch (SQLException ex) {}
        return conexao;
    }
    
    private Conexao(){
        usuario_mysql = "root";
        senha_mysql = "";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(banco, usuario_mysql, senha_mysql);
        }
        catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
        catch(Exception ex) {}
    }
    
    public Connection getConexao(){
        return this.conn;
    }
}
