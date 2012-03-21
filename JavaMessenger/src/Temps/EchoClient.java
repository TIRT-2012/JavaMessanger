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

public class EchoClient {
    
    public static void main(String[] args) {
            System.setProperty("javax.net.ssl.trustStore","testKey");
            System.setProperty("javax.net.ssl.trustStorePassword","tester");
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket s = (SSLSocket) factory.createSocket("localhost", 9999);
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);
            String sTmp = null;
            while ((sTmp = br.readLine()) != null) {
                bw.write(sTmp + '\n');
                bw.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
