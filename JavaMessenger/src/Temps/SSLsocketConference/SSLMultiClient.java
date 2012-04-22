/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketConference;

import GUI.MessegerFrame;
import Others.JMHelper;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.net.ssl.*;

/**
 *
 * @author SysOp
 */
public class SSLMultiClient {

    private static int PORT = 5050;
    private SSLSocketFactory factory = null;
    private SSLSocket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut1 = null;
    private DataOutputStream streamOut2 = null;
    private String host1 = null;
    private String host2 = null;
    private HashMap sockets;

    public SSLMultiClient() {
        System.setProperty("javax.net.ssl.trustStore", "testKey");
        System.setProperty("javax.net.ssl.trustStorePassword", "tester");
        sockets = new HashMap();
    }

    public void setIP(String ip) {
        host1 = ip;
    }

    public void prepare() {
        //host = JMHelper.getMyPublicIP();
        //host = "156.17.247.212"; //dla polaczen zdalnych wpisz adres ip
        //host = "83.5.234.211";
        //host = "83.5.165.184";
        //host = "192.168.1.102";
        //host = "156.17.247.151";
        //host ="156.17.140.151";
        System.out.println("Establishing connection. Please wait ...");
        try {
            host1 = "5.132.15.195";
            host2 = "5.198.230.181";
            
            setSocketToMap(getNewSocket(host1, PORT));
            setSocketToMap(getNewSocket(host2, PORT));
            System.out.println("Connected: " + getSocketFromMap(host1));
            System.out.println("Connected: " + getSocketFromMap(host2));
            
            console = new DataInputStream(System.in);
            
            streamOut1 = new DataOutputStream(getSocketFromMap(host1).getOutputStream());
            streamOut2 = new DataOutputStream(getSocketFromMap(host2).getOutputStream());

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
                streamOut1.writeUTF(line);
                streamOut2.writeUTF(line);
                streamOut1.flush();
                streamOut2.flush();
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
            if (streamOut1 != null) {
                streamOut1.close();
            }
            if (streamOut2 != null) {
                streamOut2.close();
            }
            if (getSocketFromMap(host1) != null) {
                getSocketFromMap(host1).close();
            }
            if (getSocketFromMap(host2) != null) {
                getSocketFromMap(host2).close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    public void setSocketToMap(SSLSocket socket) {
        System.out.println(" Pozycja na hashMap " + socket.getPort());
        this.sockets.put(getIpFromInetAddress(socket), socket);
        System.out.println(" Pozycja na hashMap " + sockets.size());
    }

    public SSLSocket getSocketFromMap(String ip) { // niedokończone
        return (this.sockets.get(ip) != null) ? (SSLSocket) sockets.get(ip) : null;
    }

    public boolean isSocketInMap(String ip) {
        return (this.sockets.get(ip) != null) ? true : false;
    }

    public String getIpFromInetAddress(SSLSocket socket) {
        InetAddress ip = socket.getInetAddress(); // '\192.168.0.1'
        String ipAdress = ip.toString().substring(1);
        return ipAdress;
    }

    public SSLSocket getNewSocket(String host, int port) throws IOException {
        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        return socket = (SSLSocket) factory.createSocket(host, PORT);
    }
}
