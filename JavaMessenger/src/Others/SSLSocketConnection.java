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
 * Klasa SSLSocketConnection to klasa "nasłuchująca" zdarzenia na serwerze od
 * klientów zewnętrznych. Pozwala na odbieranie kluczy szyfrujących i
 * odszyfrowywaniu wiadomości, plików
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
    private boolean notBegginer = false;
    private boolean isFileSender = false;

    /**
     * Konstruktor SSLSocketConnection
     *
     * @param evt
     */
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

    public boolean isNotBegginer() {
        return notBegginer;
    }

    public void setNotBegginer(boolean notBegginer) {
        this.notBegginer = notBegginer;
    }

    /**
     * Setter dodający okienko wiadomość dla odbiorcy wiadomośći od klienta
     * zewnętrznego, który zainicjował rozmowę
     *
     * @param evt
     */
    public void setFrame(MessegerFrame messenger) {
        this.messenger = messenger;
        this.messenger.setMessage("New client joined on port: " + id + " IP: " + ipAdress);
        this.messenger.setLocationRelativeTo(messenger.getRootPane());
        this.messenger.setVisible(true);
    }

    /**
     * Metoda run wątku głównego klasy SSLSocketConnection. Metoda run odczytuje
     * klucz publiczny, który używa do deszyfracji wiadomości tekstowej bądź
     * plików.
     *
     * @param evt
     */
    @Override
    public void run() {
        InputStream is = null;
        try {
            while (keepRunning) {
                System.out.println("FIRSTTIME" + isPublicKeyTransfer);
                if (isPublicKeyTransfer) {
                    spk = (SerialPublicKey) ois.readObject();
                    if (notBegginer) {
                        this.messenger.getSslControler().setAlgorithm(spk.getAlgorithm());
                        this.messenger.getSslControler().setKeySize(spk.getSymetricKeySize());
                        this.messenger.setAlgorithm(spk.getAlgorithm());
                        this.messenger.setSymetricKeySize(spk.getSymetricKeySize());

                        System.out.println("Algorytm wedlug messengera to : " + this.messenger.getAlgorithm());
                        System.out.println("DlugoscKlucza wedlug messengera to : " + this.messenger.getSymetricKeySize());
                    } else {
                        this.messenger.setAlgorithm(this.sslServer.getSslControler().getAlgorithm());
                        this.messenger.setSymetricKeySize(this.sslServer.getSslControler().getKeySize());
                    }
                    //this.messenger.getSSLClient().setSerialPublicKey(spk);
                    this.messenger.setPublicKey(spk.getPublicKey());
                    System.out.println("Klucz ODEBRANY : " + this.messenger.getPublicKey());
                    isPublicKeyTransfer = false;

                } else if (isFile) {

                    System.out.println("File transmission in process");
                    int filesize = 11;
                    long start = System.currentTimeMillis();
                    int bytesRead;
                    int current = 0;
                    byte[] mybytearray = new byte[filesize];
                    is = socket.getInputStream();
                    FileOutputStream fos = new FileOutputStream("pliczek.txt");
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    System.out.println("Bytes was read.");
                    System.out.println(bytesRead);
                    current = bytesRead;
                    System.out.println("bytesRead" + bytesRead);
                    do {
                        bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                        System.out.println("Bytes was copied to bytesRead");
                        if (bytesRead >= 0) {
                            current += bytesRead;
                        }
                    } while (bytesRead > -1);
                    System.out.println("Bytes was read to copy.");
                    bos.write(mybytearray, 0, mybytearray.length);
                    System.out.println("Bytes was written.");
                    bos.flush();
                    bos.close();
                    long end = System.currentTimeMillis();
                    System.out.println(end - start);
                    this.isFile = false;

                } else {

                    SerialCryptedMessage sCm = null;
                    String algorithm = this.messenger.getAlgorithm();
                    int keysize = this.messenger.getSymetricKeySize();
                    JCECrypter jce = new JCECrypter(algorithm, keysize);
                    sCm = (SerialCryptedMessage) ois.readObject();
                    ByteArrayInputStream in2 = new ByteArrayInputStream(sCm.getByteArray());
                    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                    try {
                        jce.decrypt(this.messenger.getSSLClient().getKeyPair().getPrivate(), in2, out2);
                    } catch (Exception ex) {
                        Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String words = out2.toString();
                    System.out.println("WORDS " + words);
                    isFileSender = words.contains("<<%file%>>");
                    //decideIsFile(isFileSender, words);
                    if (isFileSender) {
                        String fileName = words.substring(10);
                        System.out.println("Jestem tutaj");
                        sCm = (SerialCryptedMessage) ois.readObject();
                        ByteArrayInputStream in3 = new ByteArrayInputStream(sCm.getByteArray());
                        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
                        try {
                            jce.decrypt(this.messenger.getSSLClient().getKeyPair().getPrivate(), in3, out3);
                        } catch (Exception ex) {
                            Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        convertToFile(out3, fileName);
                    } else {
                        out.println("Connection " + id + ": " + words);
                        messenger.setMessage("Connection with " + ipAdress + " ," + messenger.getProfilName());
                        messenger.setMessage(words);
                        messenger.getjTextArea1().setCaretPosition(0);
                        messenger.getjTextArea1().requestFocus();
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SSLSocketConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeConnection();
    }

    public synchronized void quit() {
        keepRunning = false;
    }

    /**
     * Metoda closeConnection() zamyka socket i bufor streamu 
     *
     * @param evt
     */
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

    public InetAddress getIp() {
        return ip;
    }

    /**
     * Metoda decideIsFile() sprawdza czy wysłany obiekt to wiadomość bądź plik
     *
     * @param evt
     */
    public void decideIsFile(boolean isFileSender, String words) {
        if (isFileSender) {
            this.isFile = true;
        } else {
            out.println("Connection " + id + ": " + words);
            messenger.setMessage("Connection with " + ipAdress + " ," + messenger.getProfilName());
            messenger.setMessage(words);
            messenger.getjTextArea1().setCaretPosition(0);
            messenger.getjTextArea1().requestFocus();
        }
    }

    /**
     * Metoda convertToFile() konwertuje ciąg bajtów do tablicy bajtów i tworzy plik do wysłania.
     * @param evt
     */
    public void convertToFile(ByteArrayOutputStream out, String fileName) throws FileNotFoundException, IOException {
        System.out.println(fileName);
        byte[] temp = out.toByteArray();
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(temp);
        fos.close();
    }
}
