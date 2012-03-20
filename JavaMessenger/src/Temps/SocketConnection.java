/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import static java.lang.System.out;

/**
 *
 * @author SysOp
 */
public class SocketConnection extends Thread {

    private Socket socket;
    private int id;
    private DataInputStream streamIn;
    private boolean keepRunning;

    public SocketConnection(Socket socket) throws IOException {
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
