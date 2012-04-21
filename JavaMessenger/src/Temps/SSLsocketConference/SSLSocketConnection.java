/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketConference;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import static java.lang.System.out;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author SysOp
 */
public class SSLSocketConnection extends Thread {

    private SSLSocket socket;
    private int id;
    private DataInputStream streamIn;
    private boolean keepRunning;

    public SSLSocketConnection(SSLSocket socket) throws IOException {
        this.socket = socket;
        id = socket.getPort();
        keepRunning = true;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out.println("New client joined on port: " + id);
    }

    @Override
    public void run() {
        try {
            while (keepRunning) {
                out.println("Connection " + id + ": " + streamIn.readUTF());
            }
            out.println("Client from port " + id + " quits");
            socket.close();
        } catch (IOException ex) {
            out.println("Connection " + id + ": " + "IO Exception occured");
        }
    }

    public synchronized void quit() {
        keepRunning = false;
    }

    @Override
    public long getId() {
        return id;
    }
}
