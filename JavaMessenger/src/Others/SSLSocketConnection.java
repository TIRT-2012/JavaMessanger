/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.MessegerFrame;
import crypto.JCECrypter;
import crypto.SerialCryptedMessage;
import crypto.SerialPublicKey;
import java.io.*;
import static java.lang.System.out;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

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
    private boolean firstTime = true;
    private ObjectInputStream ois;
    private SerialPublicKey spk;

    public SSLSocketConnection(SSLSocket socket, SSLServer sslServer) throws IOException {
        this.socket = socket;
        id = socket.getPort();
        ip = socket.getInetAddress(); // '\192.168.0.1'
        ipAdress = ip.toString().substring(1);
        this.sslServer = sslServer;
        keepRunning = true;
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        ois = new ObjectInputStream(socket.getInputStream());
        out.println("New client joined on port: " + id + " IP: " + ipAdress);
    }

    public void setFrame(MessegerFrame messenger) {
        this.messenger = messenger;
        this.messenger.setMessage("New client joined on port: " + id + " IP: " + ipAdress);
        this.messenger.setLocationRelativeTo(messenger.getRootPane());
        this.messenger.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (keepRunning) {
                System.out.println("FIRSTTIME" + firstTime);
                String words = null;
                if (firstTime) {
//                    try {
//                        ois = new ObjectInputStream(socket.getInputStream());
//                    } catch (IOException ex) {
//                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    try {
                        spk = (SerialPublicKey) ois.readObject();
                    } catch (IOException ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.messenger.getSSLClient().setSerialPublicKey(spk);
                    System.out.println("Klucz publiczny to : " + spk.getPublicKey().toString());
                    firstTime = false;
                } else {
                    SerialCryptedMessage sCm = null;
                    try {
                        //decrypting
                        sCm = (SerialCryptedMessage) ois.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    words = new String(sCm.getByteArray());
                }
//                ByteArrayInputStream in2 = new ByteArrayInputStream(words.getBytes());
//                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
//                JCECrypter jce = new JCECrypter();
//                try {
//                    jce.decrypt(this.messenger.getSSLClient().getKeyPair().getPrivate(), in2, out2);
//                } catch (Exception ex) {
//                    Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.out.println("Zdeszyfrowana wiadomość: " + out2.toString());
                //////////////////


                //String words = streamIn.readUTF();
                out.println("Connection " + id + ": " + words);
                messenger.setMessage("Connection with " + ipAdress + " ," + messenger.getProfilName());
                messenger.setMessage(words);
                messenger.getjTextArea1().setCaretPosition(0);
                messenger.getjTextArea1().requestFocus();
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
