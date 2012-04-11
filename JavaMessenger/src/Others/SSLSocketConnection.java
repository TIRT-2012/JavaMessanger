/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.MessegerFrame;
import Logic.SSLController;
import Temps.SSLsocket.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import static java.lang.System.out;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JFrame;

/**
 *
 * @author SysOp
 */
public class SSLSocketConnection extends Thread {

    private SSLSocket socket;
    private int id = 0;
    private DataInputStream streamIn;
    private boolean keepRunning = false;
    MessegerFrame messenger = null;
    InetAddress ip = null;
    String ipAdress = null;
    SSLServer sslServer = null;

//    public SSLSocketConnection(SSLSocket socket ) throws IOException {
//        this.socket = socket;
//        id = socket.getPort();
//        ip = socket.getInetAddress(); // '\192.168.0.1'
//        ipAdress = ip.toString().substring(1); // przetestowac
//        this.sslServer = sslServer;
//        keepRunning = true;
//        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//        out.println("New client joined on port: " + id + " IP: " + ipAdress);
//     }
    
    public SSLSocketConnection(SSLSocket socket, SSLServer sslServer) throws IOException {
        this.socket = socket;
        id = socket.getPort();
        ip = socket.getInetAddress(); // '\192.168.0.1'
        ipAdress = ip.toString().substring(1); // przetestowac
        this.sslServer = sslServer;
        keepRunning = true;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out.println("New client joined on port: " + id + " IP: " + ipAdress);
    }
    
    public SSLSocketConnection(SSLSocket socket, MessegerFrame messenger, SSLServer sslServer) throws IOException {
        this.socket = socket;
        id = socket.getPort();
        ip = socket.getInetAddress(); // '\192.168.0.1'
        ipAdress = ip.toString().substring(1); // przetestowac
        this.sslServer = sslServer;
        keepRunning = true;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out.println("New client joined on port: " + id + " IP: " + ipAdress);
        this.messenger = messenger;
        this.messenger.setMessage("New client joined on port: " + id + " IP: " + ipAdress);
        this.messenger.setLocationRelativeTo(messenger.getRootPane());
        this.messenger.changeJLabel1(messenger.getProfilName());
        this.messenger.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (keepRunning) {
                String words = streamIn.readUTF();
                out.println("Connection " + id + ": " + words);
                messenger.setMessage("Connection with " + ipAdress + " ," + messenger.getProfilName());
                messenger.setMessage(words);
            }
            out.println("Client from port " + id + " quits");
            closeConnection();
        } catch (IOException ex) {
            out.println("Connection " + id + ": " + "IO Exception occured");
            messenger.setMessage("Connection with " + ipAdress + " is occured. " + "IO Exception occured");
        }

    }

    public synchronized void quit() {
        keepRunning = false;
    }

    public final void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (streamIn != null) {
                streamIn.close();
            }
        } catch (IOException ex) {
        }
    }

    @Override
    public long getId() {
        return id;
    }
}
