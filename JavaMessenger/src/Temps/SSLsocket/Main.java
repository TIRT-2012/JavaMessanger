/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocket;

/**
 *
 * @author Piotr
 */
public class Main {
    
    public static void main(String args[])
    {
        SSLConnector sslc = new SSLConnector(false);
        SSLConnector sslc2 = new SSLConnector(true);
        SSLConnector sslc3 = new SSLConnector(true);
        
        sslc.run();
        sslc2.run();
        sslc3.run();
    }
}
