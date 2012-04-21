/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketConference;

/**
 *
 * @author SysOp
 */
import Temps.SocketConnection;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;
import javax.net.ssl.*;

public class SSLServer implements Runnable {

    private static int MAX = 50;
    private static int PORT = 5050;
    private SSLServerSocketFactory factory = null;
    private SSLServerSocket server = null;
    private SSLSocket socket = null;
    private DataInputStream streamIn = null;
    private SSLSocketConnection[] connections = new SSLSocketConnection[MAX];
    
    public SSLServer() {
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
    }

    public void prepare() {
        try {
            out.println("Binding to port " + PORT + ", please wait  ...");
            factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket) factory.createServerSocket(PORT, MAX, null);
            out.println("SocketAddr: " + server.getLocalSocketAddress());
            out.println("Server running: " + server);
            
            
        } catch (IOException ioe) {
            out.println(ioe);
        }
    }

    public final void close() {
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
    public void run() {
        while (true) {
            try {
                addConnection(new SSLSocketConnection((SSLSocket) server.accept()));
            } catch (IOException ex) {
                out.println("Server: IO Exception occured");
            }
        }
    }

    private void addConnection(SSLSocketConnection sc) {
        boolean iterate = true;
        int i = 0;
        while (iterate && i < MAX) {
            if (connections[i] == null) {
                connections[i] = sc;
                iterate = false;
                sc.start();
            } else {
                i++;
            }
        }
    }

    private void closeConnection(SocketConnection sc) {
        sc.quit();
        for (int i = 0; i < connections.length; i++) {
            if (connections[i].getId() == sc.getId()) {
                connections[i] = null;
            }
        }
    }
    //OLD
//    public void run() {
//        boolean done = false;
//        while (!done) {
//            try {
//                String line = streamIn.readUTF();
//                out.println(line);
//                done = line.equals(".bye");
//            } catch (IOException ioe) {
//                done = true;
//            }
//        }
//        close();
//    }
}
