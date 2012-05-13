/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocket;

import Others.ApplicationComponents;
import Others.JMHelper;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.*;

/**
 *
 * @author SysOp
 */
public class SSLClient {

    private static int PORT = 5050;
    private SSLSocketFactory factory = null;
    private SSLSocket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private String host;

    public SSLClient() {
        System.setProperty("javax.net.ssl.trustStore", "testKey");
        System.setProperty("javax.net.ssl.trustStorePassword", "tester");
    }

    public void setIP(String ip)
    {
        host = ip;
    }
    
    public void prepare() {
        String user = ApplicationComponents.getInstance().getLoginController().getUserObject().getUserName();
            if (user.equals("htesto")) {
                host = "5.118.168.127";
            } else if (user.equals("hwind44")) {
                host = "5.126.123.85";
            } else if (user.equals("hpiotrek")) {
                host = "5.198.230.181";
            } else if (user.equals("hpiotrek89")) {
                host = "5.132.15.195";
            } else {
                host = JMHelper.getMyPublicIP();
            }
        //host = "156.17.247.212"; //dla polaczen zdalnych wpisz adres ip
        //host = "83.5.234.211";
        //host = "83.5.165.184";
        //host = "192.168.1.102";
        //host = "156.17.247.151";
        //host ="156.17.140.151";
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(host, PORT);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());

        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void run() {
        String line = "";
        while (!line.equals(".bye")) {
            try {
                line = console.readLine();
                streamOut.writeUTF(line);
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
        close();
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
