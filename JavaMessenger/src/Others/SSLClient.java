/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.FileSender;
import Logic.*;
import GUI.MessegerFrame;
import Temps.SSLsocket.*;
import Others.JMHelper;
import crypto.JCECrypter;
import crypto.SerialCryptedMessage;
import crypto.SerialPublicKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.*;

/**
 * Klasa SSLClient to moduł klient w architekturze klient-serwer. Klasa
 * SSLClienta umożliwia użytkownikowi frontowemu przesyłanie wiadomości
 * tekstowych, plików oraz streamin audio
 *
 * @author SysOp
 */
public class SSLClient {

    private static int PORT = 5050;
    private SSLSocketFactory factory = null;
    private SSLSocket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private String host;
    private ObjectOutputStream oos;
    private KeyPair keyPair = null;
    private SerialPublicKey serialPublicKey = null;
    private OutputStream output = null;
    private byte byteArray[] = null;
    private FileSender fileSender = null;

    /**
     * Konstruktor klienta, który tworzy bezpieczene połączenie SSL na podstawie
     * wygenerowanego klucza i certyfikatu weryfkującego klucz od serwera
     *
     * @param evt
     */
    public SSLClient() {
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
    }

    /**
     * Konstruktor klienta, który tworzy bezpieczene połączenie SSL na podstawie
     * wygenerowanego klucza i certyfikatu weryfkującego klucz od serwera.W
     * konstruktorze tym dodatkowo tworzymy socket i zabezpieczamy szyfrowanie
     * SSL
     *
     * @param evt
     */
    public SSLClient(String serverhost) {
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
        //host = JMHelper.getMyPublicIP(); //dla polaczen zdalnych wpisz adres ip
        String feedbackFromServer = "Approved connection";
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(serverhost, PORT);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
        try {
            streamOut.writeUTF(feedbackFromServer);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
        }

    }

    /**
     * Metoda ustawiająca ip clienta
     *
     * @param evt
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Metoda, która zabezpiecza połączenie SSL, tworzy szyfrowany socket
     *
     * @param evt
     */
    public void prepare() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(host, PORT);
            System.out.println("Connected: " + socket);
            console = new DataInputStream(System.in);
            streamOut = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    /**
     * Metoda, która pozwala na uruchomienie przepływu wiadomości.
     *
     * @param evt
     */
    public void run() {
        try {
            //String s = messeger.getMessage(); dodac obsluge z przycisku
            //streamOut.writeUTF(message);
            oos.flush();
            //streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
        }
    }

    /**
     * Metoda, która pozwala na zwrócenie odpowiedzi od Serwera klienta
     * zewnętrznego. Przy ustanawianiu połączenia
     *
     * @param evt
     */
    public void sendFeedback() {
        try {
            streamOut.writeUTF("Client Approved");
        } catch (IOException ex) {
            Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda zamykająca połączenie - kasuje soket, bufor streamu
     *
     * @param evt
     */
    public void close() {
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    public String getHost() {
        return host;
    }

    public SSLSocket getSocket() {
        return socket;
    }

    public DataOutputStream getStreamOut() {
        return streamOut;
    }

    public SerialPublicKey getSerialPublicKey() {
        return serialPublicKey;
    }

    public void setSerialPublicKey(SerialPublicKey serialPublicKey) {
        this.serialPublicKey = serialPublicKey;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    /**
     * Metoda umożliwiająca wysłanie klucza do serwera klienta zewnętrznego, po
     * to by serwer ten mógł odczytać szyfrowaną wiadomosć
     *
     * @param evt
     */
    public void sendKey() {
        System.out.println("sendKey()");
        try {
            System.out.println("serialPublicKey PUBLICKEY: " + serialPublicKey.getPublicKey());
            System.out.println("serialPublicKey ALGORITHM: " + serialPublicKey.getAlgorithm());
            System.out.println("serialPublicKey KEYSIZE" + serialPublicKey.getSymetricKeySize());
            oos.writeObject(serialPublicKey);
            //oos.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        serialPublicKey = null;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return oos;
    }
}
