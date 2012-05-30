/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.MessegerFrame;
import Logic.SSLController;
import crypto.JCECrypter;
import crypto.SerialPublicKey;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import static java.lang.System.out;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.*;

/**
 * Klasa SSLServer to klasa modułu Serwera architektury klient-serwer opartej na
 * szyfrowaniu SSL Obiekt ten decyduje o wysyłaniu kluczy szyfrujących,
 * kierowaniu odpowiedzi do klientów przy inicjacji połączenia oraz odbieraniu
 * wiadomości od klientów zewnętrznych
 *
 * @author SysOp
 */
public class SSLServer implements Runnable {

    private static int MAX = 50;
    private static int PORT = 5050;
    private static int TIMEOUT = 100;
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

    /**
     * Konstruktor, w którym następuje akceptacja kluczy dla certyfikatu SSL.
     * Konstruktor tworzy listę modułów, które przesyłają informacje do klientów
     * wewnętrznych
     *
     * @param evt
     */
    public SSLServer(SSLController sslControler) {
        this.sslControler = sslControler;
        messengerFrameList = new HashMap();
        System.setProperty("javax.net.ssl.keyStore", "testKey");
        System.setProperty("javax.net.ssl.keyStorePassword", "tester");
        System.setProperty("javax.net.ssl.trustStore", "testKey");
    }

    /**
     * Metoda prepare nawiązuje połaczenie socketowe na łączu SSL.
     *
     * @param evt
     */
    public void prepare() {
        try {
            System.out.println("Binding to port " + PORT + ", please wait  ...");
            factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket) factory.createServerSocket(PORT, MAX, null);
            server.setSoTimeout(TIMEOUT);
            System.out.println("SocketAddr: " + server.getLocalSocketAddress());
            System.out.println("Server running: " + server);

            String user = ApplicationComponents.getInstance().getLoginController().getUserObject().getUserName();
            if (user.equals("htesto")) {
                myIp = "5.118.168.127";
            } else if (user.equals("hwind44")) {
                myIp = "5.126.123.85";
            } else if (user.equals("hpiotrek")) {
                myIp = "5.198.230.181";
            } else if (user.equals("hpiotrek89")) {
                myIp = "5.132.15.195";
            } else {
                myIp = JMHelper.getMyPublicIP();
            }
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

    /**
     * Run umożliwia uruchomienie serwera. Serwer SSL jest włączony w momencie
     * zalogowania użytkownika. Metoda run steruje funkcjami Serwera tj. śledzi
     * odpowiedzi od klientów zewnętrznych, dodaje ich do listy, łaczy się z
     * nimi, przydziela wiadomości do klientów wewnętrznych. Wysyła również
     * klucz publiczny klientom zewnętrznym. Deszyfruje również wiadomości od
     * klientów zewnętrznych. Metoda Run() za każdym razem, kiedy przychodzi
     * nowy klient zewnętrzny tworzy połączenie socketowe jako osobny wątek w
     * klasie SSLSocketConnection.
     *
     * @param evt
     */
    @Override
    public void run() {
        while (keepRunning()) {
            try {
                try {
                    socket = (SSLSocket) server.accept();
                } catch (SocketTimeoutException ste) {
                    //ignore timeout
                }
                if (socket != null) {
                    InetAddress ipTemp = socket.getInetAddress();
                    ipAdress = ipTemp.toString().substring(1);

                    boolean flag = isClientInClientsMap(ipAdress);
                    ////
                    boolean isClientReceiver = (!this.isFrameInMap(ipAdress) && !flag);
                    System.out.println("KLient zewnętrzny uruchomiony" + isClientReceiver);
                    ////
                    MessegerFrame mf = new MessegerFrame(sslControler);
                    mf.setVisible(false);

                    sslcc = new SSLSocketConnection(socket, this);
                    sslcc.setFrame(mf);
                    addConnection(sslcc);

                    socket = null;

                    if (isClientReceiver) {
                        this.sslcc.setNotBegginer(true);
                        this.sslControler.runClient(ipAdress);
                        System.out.println("klient zewnetrzny uruchomiony ");
                        //odbierz wiadomośc od klienta, który zapoczątkował
                    }

                    mf.setIp(ipAdress);
                    mf.addSSLClient(sslControler.getClient(ipAdress));
                    mf.addSSLSocketConnection(sslcc);
                    mf.changeJLabel1(sslControler.getUserName(ipAdress));
                    this.setFrameToMap(mf);

                    // START sending public key
                    String algorithm = sslControler.getAlgorithm();
                    System.out.println("SSLSEVER: Algorithm - " + algorithm);
                    int keySize = sslControler.getKeySize();
                    System.out.println("SSLSEVER: Keysize - " + keySize);
                    JCECrypter cryptograph = new JCECrypter(algorithm, keySize);
                    KeyPair RSAKey = null;
                    try {
                        RSAKey = cryptograph.generateRSAKey();
                    } catch (Exception ex) {
                        Logger.getLogger(SSLServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    sslControler.getClient(ipAdress).setKeyPair(RSAKey);
                    SerialPublicKey publicKey = new SerialPublicKey(RSAKey.getPublic());
                    /////
                    if (!isClientReceiver) {
                        publicKey.setAlgorithm(algorithm);
                        publicKey.setSymetricKeySize(keySize);
                    }
                    ////
                    sslControler.getClient(ipAdress).setSerialPublicKey(publicKey);
                    System.out.println("Moj klucz to" + publicKey.getPublicKey());
                    sslControler.getClient(ipAdress).sendKey();
                    System.out.println("Adres hosta: " + sslControler.getClient(ipAdress).getHost());
                    // END sending public key
                }
            } catch (IOException ex) {
                out.println("Server: IO Exception occured");
            }

        }
        closeServer();
    }

    /**
     * Metoda dodająca połaczenie socketowe z klientem zewnętrznym. Uruchamia
     * wątek tego połaczenia.
     *
     * @param evt
     */
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

    public SSLSocketConnection getSSLSocketConnectionController() {
        return sslcc;
    }

    /**
     * Metoda dodająca Obiekt typu MessegerFrame do listy okienek. Pozwala to na
     * przerzucenie wiadomości od klientów zewnętrznych do klientów wewnętrznych
     *
     * @param evt
     */
    public void setFrameToMap(MessegerFrame messenger1) {
        System.out.println(" Pozycja na hashMap " + messenger1.getIp());
        this.messengerFrameList.put(messenger1.getIp(), messenger1);
        System.out.println(" Pozycja na hashMap " + messengerFrameList.size());
    }

    public MessegerFrame getFrameFromMap(String ip) { // niedokończone
        return (this.messengerFrameList.get(ip) != null) ? (MessegerFrame) messengerFrameList.get(ip) : null;
    }

    /**
     * Metoda sprawdzająca czy istnieje okienko wiadomości typu MessengerFrame
     * na liście okienek, uprzednio podając ip jako identyfikator
     *
     * @param evt
     */
    public boolean isFrameInMap(String ip) {
        return (this.messengerFrameList.get(ip) != null) ? true : false;
    }

    /**
     * Metoda sprawdzająca czy istnieje klient na liście klientów Serwera na
     * liście okienek, uprzednio podając ip jako identyfikator
     *
     * @param evt
     */
    public boolean isClientInClientsMap(String ip) {
        return (sslControler.getClient(ipAdress) != null) ? true : false;
    }

    public SSLController getSslControler() {
        return sslControler;
    }

    /**
     * Zamykająca Server. Wykonywana przy wylogowaniu bądź zakończeniu aplikacji
     *
     * @param evt
     */
    private void closeServer() {
        closeAll();
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void closeAll() {
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null) {
                closeConnection(connections[i]);
            }
        }
    }

    /**
     * Metoda zatrzymująca wątki klasy SSLSOcketConnection. Pozwala na
     * zakończenie nasłuchiwania klienta zewnętrznego przez Serwer
     *
     * @param evt
     */
    private void closeConnection(SSLSocketConnection sc) {
        System.out.println("closeConnection() ");
        sc.quit(); // zamyka socket sslcocketconnection czyli wątek pobierania danych od clienta
        for (int i = 0; i < connections.length; i++) {
            if (connections[i].getId() == sc.getId()) {
                connections[i] = null;
            }
        }
    }

    public synchronized void stopRunning() {
        keepRunning = false;
    }

    private synchronized boolean keepRunning() {
        return keepRunning;
    }
}
