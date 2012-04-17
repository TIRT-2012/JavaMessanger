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
    private boolean isPublicKeyTransfer = true;
    private ObjectInputStream ois;
    private SerialPublicKey spk;
    private boolean isFile = false;
    private boolean isClose = false;

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
                System.out.println("FIRSTTIME" + isPublicKeyTransfer);
                if (isPublicKeyTransfer) {
                    try {
                        spk = (SerialPublicKey) ois.readObject();
                    } catch (IOException ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.messenger.getSslControler().setAlgorithm(spk.getAlgorithm());
                    this.messenger.getSslControler().setKeySize(spk.getSymetricKeySize());
                    this.messenger.setPublicKey(spk.getPublicKey());
                    this.messenger.setAlgorithm(spk.getAlgorithm());
                    this.messenger.setSymetricKeySize(spk.getSymetricKeySize());
                    //this.messenger.getSSLClient().setSerialPublicKey(spk);
                    System.out.println("Klucz publiczny to : " + spk.getPublicKey().toString());
                    System.out.println("Klucz publiczny wedlug messengera to : " + this.messenger.getPublicKey());
                    System.out.println("Algorytm wedlug messengera to : " + this.messenger.getAlgorithm());
                    System.out.println("DlugoscKlucza wedlug messengera to : " + this.messenger.getSymetricKeySize());
                    isPublicKeyTransfer = false;
                } else {
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
                    } else {
                        SerialCryptedMessage sCm = null;
//                    try {
//                        //decrypting
//                        sCm = (SerialCryptedMessage) ois.readObject();
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    String words = new String(sCm.getByteArray());
                        JCECrypter jce = new JCECrypter(this.messenger.getAlgorithm(), this.messenger.getSymetricKeySize());
                        System.out.println("Socket connection public key: "+this.messenger.getPublicKey().toString());
                        try {
                            sCm = (SerialCryptedMessage) ois.readObject();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ByteArrayInputStream in2 = new ByteArrayInputStream(sCm.getByteArray());
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        try {
                            jce.decrypt(this.messenger.getSSLClient().getKeyPair().getPrivate(), in2, out2);
                        } catch (Exception ex) {
                            Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String words = out2.toString();
                        out.println("Connection " + id + ": " + words);
                        messenger.setMessage("Connection with " + ipAdress + " ," + messenger.getProfilName());
                        messenger.setMessage(words);
                        messenger.getjTextArea1().setCaretPosition(0);
                        messenger.getjTextArea1().requestFocus();
                    }

                }
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
