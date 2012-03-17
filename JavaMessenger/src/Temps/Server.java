/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

/**
 *
 * @author SysOp
 */
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;

public class Server {

    private static int PORT = 5050;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;

    public void prepare() {
        try {
            out.println("Binding to port " + PORT + ", please wait  ...");
            server = new ServerSocket(PORT, 1, null);
            out.println("InetAddr: "+server.getInetAddress());
            out.println("SocketAddr: "+server.getLocalSocketAddress());
            out.println("Server started: " + server);
            out.println("Waiting for a client ...");
            socket = server.accept();
            out.println("Client accepted: " + socket);
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
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

    public void run() {
        boolean done = false;
        while (!done) {
            try {
                String line = streamIn.readUTF();
                out.println(line);
                done = line.equals(".bye");
            } catch (IOException ioe) {
                done = true;
            }
        }
        close();
    }
}
