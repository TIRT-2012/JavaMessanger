/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketFileTransmission;

/**
 *
 * @author SysOp
 */
import Temps.SocketConnection;
import Temps.SocketConnection;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;
import javax.net.ssl.*;

public class SSLServer_1 implements Runnable {

    private boolean isFile = true;
    private static int MAX = 50;
    private static int PORT = 5050;
    private SSLServerSocketFactory factory = null;
    private SSLServerSocket server = null;
    private SSLSocket socket = null;
    private DataInputStream streamIn = null;
    private SSLSocketConnection_1[] connections = new SSLSocketConnection_1[MAX];

    public SSLServer_1() {
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
                socket = (SSLSocket) server.accept();
                addConnection(new SSLSocketConnection_1(socket));
                if (isFile) {

                    int filesize = 6022386;
                    long start = System.currentTimeMillis();
                    int bytesRead;
                    int current = 0;
                    byte[] mybytearray = new byte[filesize];
                    InputStream is = socket.getInputStream();
                    FileOutputStream fos = new FileOutputStream("mojakopia.mp3");
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
                    bos.write(mybytearray, 0, mybytearray.length);
                    bos.flush();
                    long end = System.currentTimeMillis();
                    System.out.println(end - start);
                    bos.close();
                    socket.close();


                }
            } catch (IOException ex) {
                out.println("Server: IO Exception occured");
            }
        }
    }

    private void addConnection(SSLSocketConnection_1 sc) {
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
 
}
