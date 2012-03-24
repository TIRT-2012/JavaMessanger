/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketFileTransmission;

import Others.JMHelper;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.*;

/**
 *
 * @author SysOp
 */
public class SSLClient_1 {

    private static int PORT = 5050;
    private boolean isFile = true;
    private SSLSocketFactory factory = null;
    private SSLSocket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private String host;
    private OutputStream output = null;
    byte[] buffer;

    public SSLClient_1() {
        System.setProperty("javax.net.ssl.trustStore", "testKey");
        System.setProperty("javax.net.ssl.trustStorePassword", "tester");
    }

    public void prepare() {
        //host = JMHelper.getMyPublicIP(); //dla polaczen zdalnych wpisz adres ip
        host = "83.5.165.184";
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(host, PORT);
            System.out.println("Connected: " + socket);
            if (!isFile) {
                console = new DataInputStream(System.in);
                streamOut = new DataOutputStream(socket.getOutputStream());
            }

        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void run() {
        if (!isFile) {
            String line = "";
            while (!line.equals(".bye")) {
                try {
                    while (!line.equals(".bye")) {
                        line = console.readLine();
                        streamOut.writeUTF(line);
                        streamOut.flush();
                    }
                } catch (IOException ioe) {
                    System.out.println("Sending error: " + ioe.getMessage());
                }

            }
            close();
        } else {
            try {
                File myFile = new File("D:\\Muzyka\\Flipsyde - Someday.mp3");
                buffer = new byte[(int) myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(buffer, 0, buffer.length);
                System.out.println("test " + socket.getOutputStream());
                output = socket.getOutputStream();
                System.out.println("Sending...");
                output.write(buffer, 0, buffer.length);
                output.flush();
                socket.close();

            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
    }

    public void close() {
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}
