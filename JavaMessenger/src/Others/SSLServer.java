/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

/**
 *
 * @author SysOp
 */
import GUI.MessegerFrame;
import Logic.SSLController;
import Temps.Client;
import Temps.SSLsocket.*;
import Temps.SocketConnection;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.*;
import javax.swing.JFrame;

public class SSLServer implements Runnable {

    private static int MAX = 50;
    private static int PORT = 5050;
    private SSLServerSocketFactory factory = null;
    private SSLServerSocket server = null;
    private SSLSocket socket = null;
    private DataInputStream streamIn = null;
    private SSLSocketConnection[] connections = new SSLSocketConnection[MAX];
    private SSLSocketConnection sslcc;
    private boolean keepRunning = true;
    private SSLController sslControler = null;
    private HashMap messengerFrameList = null;
    String myIp;
    private String ipAdress;

    public SSLServer(SSLController sslControler) {
        this.sslControler = sslControler;
        messengerFrameList = new HashMap();
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
    }

    public void prepare() {
        try {
            System.out.println("Binding to port " + PORT + ", please wait  ...");
            factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket) factory.createServerSocket(PORT, MAX, null);
            System.out.println("SocketAddr: " + server.getLocalSocketAddress());
            System.out.println("Server running: " + server);

            myIp = JMHelper.getMyPublicIP();

            //messenger.setMessage("Binding to port " + PORT + ", please wait  ...");
            //messenger.setMessage("SocketAddr: " + server.getLocalSocketAddress());
            //messenger.setMessage("Server running: " + server);
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
        try {
            while (keepRunning) {
                try {
                    socket = (SSLSocket) server.accept();
                    InetAddress ipTemp = socket.getInetAddress();
                    ipAdress = ipTemp.toString().substring(1);
                    
                    boolean flag = isClientInClientsMap(ipAdress);
                    
                    MessegerFrame mf = new MessegerFrame(sslControler);
                    mf.setVisible(false);
                    
                    sslcc = new SSLSocketConnection(socket, this);
                    sslcc.setFrame(mf);
                    addConnection(sslcc);
                    
                    if (!this.isFrameInMap(ipAdress) && !flag) {
                        this.sslControler.runClient(ipAdress);
                    }

                    mf.setIp(ipAdress);
                    mf.addSSLClient(sslControler.getClient(ipAdress));
                    mf.addSSLSocketConnection(sslcc);
                    this.setFrameToMap(mf);
                    
                    //System.out.println("ERROR : Statement unreachable");

                } catch (IOException ex) {
                    out.println("Server: IO Exception occured");
                }

            }
            try {
                Robot r = new Robot();
                r.delay(5000);
                closeAll();
                socket.close();
                server.close();
                socket = null;
                server = null;
            } catch (AWTException ex) {
                Logger.getLogger(SSLServer.class.getName()).log(Level.SEVERE, null, ex);
            }


            //close(); // zamyka socket servera i DataInputStream
            //closeConnection(sslcc);
        } catch (IOException ex) {
            Logger.getLogger(SSLServer.class.getName()).log(Level.SEVERE, null, ex);
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

    private void closeAll() {
        for (int i = 0; i < connections.length; i++) {
            closeConnection(connections[i]);
        }
    }

    public void closeConnection(SSLSocketConnection sc) {
        System.out.println("closeConnection() ");
        sc.quit(); // zamyka socket sslcocketconnection czyli wątek pobierania danych od clienta
        for (int i = 0; i < connections.length; i++) {
            if (connections[i].getId() == sc.getId()) {
                connections[i] = null;
            }
        }
    }

    public synchronized void quit() throws IOException {
        keepRunning = false;
        //server.close();
        System.out.println("quit: sslserver");
    }

    public SSLSocketConnection getSSLSocketConnectionController() {
        return sslcc;
    }

    public void setFrameToMap(MessegerFrame messenger1) {
        System.out.println(" Pozycja na hashMap " + messenger1.getIp());
        this.messengerFrameList.put(messenger1.getIp(), messenger1);
        System.out.println(" Pozycja na hashMap " + messengerFrameList.size());
    }

    public MessegerFrame getFrameFromMap(String ip) { // niedokończone
        if (this.messengerFrameList.get(ip) != null) {
            System.out.println(" Brak obiektu na ip-tej pozycji");
            return null;
        } else {
            System.out.println(" Obiekt na tej pozycji " + this.messengerFrameList.get(ip));
            return (MessegerFrame) messengerFrameList.get(ip);
        }
    }

    public boolean isFrameInMap(String ip) {
        return (this.messengerFrameList.get(ip) != null) ? true : false;
    }

    public boolean isClientInClientsMap(String ip) {
        return (sslControler.getClient(ipAdress) != null) ? true : false;
    }
}
