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
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(9999);
            SSLSocket s = (SSLSocket) ss.accept();
//            InputStreamReader isr = new InputStreamReader(s.getInputStream());
//            BufferedReader br = new BufferedReader(isr);
//            String sTmp = null;
//            while ((sTmp = br.readLine()) != null) {
//                System.out.println(sTmp);
//                System.out.flush();
//            }

            int filesize = 6022386; 
            long start = System.currentTimeMillis();
            int bytesRead;
            int current = 0;
            byte[] mybytearray = new byte[filesize];
            InputStream is = s.getInputStream();
            FileOutputStream fos = new FileOutputStream("source-copy.pdf");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (bytesRead > -1);
            bos.write(mybytearray, 0, current);
            bos.flush();
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            bos.close();
            s.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
