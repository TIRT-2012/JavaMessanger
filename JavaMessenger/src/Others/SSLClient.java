/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Logic.*;
import GUI.MessegerFrame;
import Temps.SSLsocket.*;
import Others.JMHelper;
import crypto.SerialPublicKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private ObjectOutputStream oos;
    private KeyPair keyPair = null;
    private SerialPublicKey serialPublicKey = null;

    public SSLClient() {
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
    }
    
    public SSLClient(String serverhost)
    {
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
        //host = JMHelper.getMyPublicIP(); //dla polaczen zdalnych wpisz adres ip
        String feedbackFromServer = "Approved connection";
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(serverhost, PORT);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
        try {
            streamOut.writeUTF(feedbackFromServer);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
        }
        
    }
            
    public void setHost(String host) {
        this.host = host;
    }

    public void prepare() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(host, PORT);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
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

    public void sendFeedback()
    {
        try {
            streamOut.writeUTF("Client Approved");
        } catch (IOException ex) {
            Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public SerialPublicKey getSerialPublicKey() {
        return serialPublicKey;
    }

    public void setSerialPublicKey(SerialPublicKey serialPublicKey) {
        this.serialPublicKey = serialPublicKey;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
    
    public void sendKey()
    {
        System.out.println("sendKey()");
        try {
            System.out.println("serialPublicKey: " + serialPublicKey.getPublicKey());
            oos.writeObject(serialPublicKey);
            //oos.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        serialPublicKey = null;
    }
    public ObjectOutputStream getObjectOutputStream()
    {
        return oos;
    }
}
