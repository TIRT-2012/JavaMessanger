/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Logic.*;
import GUI.MessegerFrame;
import Temps.SSLsocket.*;
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
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void prepare() {
        host = JMHelper.getMyPublicIP(); //dla polaczen zdalnych wpisz adres ip
        //host = "83.5.234.211";
        //host = "83.5.165.184";
        //host = "192.168.1.102";
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
        try {
            //String s = messeger.getMessage(); dodac obsluge z przycisku
            //streamOut.writeUTF(message);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
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

    public String getHost()
    {
        return host;
    }
    
    public DataOutputStream getStreamOut() {
        return streamOut;
    }
}
