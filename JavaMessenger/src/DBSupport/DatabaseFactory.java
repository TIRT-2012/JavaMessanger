/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import static java.lang.System.out;
/**
 *
 * @author SysOp
 */
public class DatabaseFactory {
    
    private static String host="jdbc:mysql://208.11.220.249:3306";
    private static String user="peross89";
    private static String pass="pwrpiotr89";
    
    public DatabaseFactory(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            out.println("Exception in DatabaseFactory constructor");
        }
    }
    
    public static Connection getConnection(){
        Connection conn=null;
        try{
            conn=(Connection) DriverManager.getConnection(host,user,pass);
        }
        catch(SQLException sqle){
            out.println("Exception in DatabaseFactory getConnection method");
            sqle.printStackTrace();
        }
        return conn;
    }
    
    public static void test(){
        try {
            PreparedStatement stmt=DatabaseFactory.getConnection().prepareStatement("SELECT * FROM jmcdata.users");
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                out.println(rs.getString("user_name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
