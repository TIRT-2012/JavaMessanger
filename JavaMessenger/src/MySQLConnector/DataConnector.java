/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MySQLConnector;
import java.sql.Connection;
import java.sql.*;
/**
 *
 * @author Piotr
 */
public class DataConnector {
    public static void main(String args[])
    {
            System.out.println("Test polaczenia MySQL.");
            Connection conn = null;
            String url = "jdbc:mysql://sql09.freemysql.net/";
            String dbName = "jmcdata";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "peross89"; 
            String password = "pwrpiotr89";
            try {
                Class.forName(driver).newInstance();
                String dbURL = url+ "project?user=" + userName + "&password=" + password;
                //conn = DriverManager.getConnection(url+dbName,userName,password);
                conn = DriverManager.getConnection(url+dbName,userName,password);
                //conn = DriverManager.getConnection(dbURL);
                System.out.println("Connected to the database");
                conn.close();
                System.out.println("Disconnected from database");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
}
