/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

/**
 *
 * @author SysOp
 */
import java.io.IOException;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;

public class Server implements Runnable{
    

    private static int MAX = 50;
    private static int PORT = 5050;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private SocketConnection[] connections=new SocketConnection[MAX];

    public void prepare() {
        try {
            out.println("Binding to port " + PORT + ", please wait  ...");
            server = new ServerSocket(PORT, MAX, null);
            out.println("SocketAddr: "+server.getLocalSocketAddress());
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
    public void run(){
        while(true){
            try {
                addConnection(new SocketConnection(server.accept()));
            } catch (IOException ex) {
                out.println("Server: IO Exception occured");
            }
        }
    }
    
    private void addConnection(SocketConnection sc){
        boolean iterate=true;
        int i=0;
        while(iterate && i < MAX){
            if(connections[i]==null){
                connections[i]=sc;
                iterate=false;
                sc.start();
            }
            else{
                i++;
            }
        }
    }
    
    private void closeConnection(SocketConnection sc){
        sc.quit();
        for(int i=0; i<connections.length; i++){
            if(connections[i].getId()==sc.getId()){
                connections[i]=null;
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
