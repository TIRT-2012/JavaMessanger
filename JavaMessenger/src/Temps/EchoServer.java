/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

/**
 *
 * @author Piotr
 */
import javax.net.ssl.*;
import java.io.*;
public class EchoServer {
public static void main(String[] args) {
    System.setProperty("javax.net.ssl.keyStore","testKey");
    System.setProperty("javax.net.ssl.keyStorePassword","tester");
    try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(9999);
            SSLSocket s = (SSLSocket) ss.accept();
            InputStreamReader isr = new InputStreamReader(s.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String sTmp = null;
            while ((sTmp = br.readLine()) != null) {
                System.out.println(sTmp);
                System.out.flush();
            }
        }  catch (Exception ex) {
        ex.printStackTrace();
    }
}
}
