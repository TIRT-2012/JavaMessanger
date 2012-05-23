/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.UsersDAO;
import Entities.Users;
import GUI.MessegerFrame;
import Others.ApplicationComponents;
import Others.SSLClient;
import Others.SSLServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa opowiedzialna za sterowanie architekturą client-serwer Jest używana
 * jako inicjator połączenia pomiędzy dwoma klientami. Kontroler ten ustanawia
 * połączenie szyfrowane SSL.
 *
 * @author Piotr
 */
public class SSLController {

    private boolean isServer = false;
    private SSLServer server = null;
    private SSLClient client = null;
    private HashMap clientsMap = null;
    private Thread ts;
    private MessegerFrame msgr = null;
    private ApplicationComponents applicationComponents;
    private String algorithm = null;
    private int keySize;

    /**
     * Konstruktor kontrolera, który tworzy listę klientów aktualnie
     * nawiązujących połączenie z modułem serwera
     *
     * @param evt
     */
    public SSLController(ApplicationComponents ac) {
        //runServer();
        clientsMap = new HashMap();
        applicationComponents = ac;
    }

    public ApplicationComponents getApplicationComponents() {
        return applicationComponents;
    }

    public HashMap getClientsMap() {
        return clientsMap;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    /**
     * Metoda zwracająca userName na podstawie ip
     *
     * @param evt
     */
    public String getUserName(String ip) {
        System.out.println("getUserName()");
        UsersDAO userDao = applicationComponents.getUsersDAO();
        Users temp = userDao.findByIp(ip, true);
        applicationComponents.releseDAO(userDao);
        return (String) temp.getUserName();
    }

    /**
     * Metoda inicjująca architekturę klient serwer z poziomu dwóch modułów -
     * serwa i klienta w zależności od zmiennej isClient
     *
     * @param evt
     */
    public void setSSLConnection(boolean isClient) {

        isServer = (isClient) ? false : true;
        if (isServer) {
            server = new SSLServer(this);
            ts = new Thread(server);
            server.prepare();
            ts.start();
        } else {
            client = new SSLClient();
            client.prepare();
            client.run();

        }
    }

    public SSLClient getClientInstance() {
        return client;
    }

    /**
     * Metoda uruchamiajaca server
     *
     * @param evt
     */
    public final void runServer() {
        System.out.println("runServer()");
        setSSLConnection(false);
    }

    /**
     * Metoda uruchamiajaca klienta i dodająca obiekt klasy SSLClient do listy
     * Clientów
     *
     * @param evt
     */
    public void runClient(String ip) {
        System.out.println("runClient()");
        client = new SSLClient();
        client.setHost(ip);
        client.prepare();
        client.run();
        clientsMap.put(ip, client);
    }

    /**
     * Metoda zwracająca odpowiedź od serwera do KLienta, ze można ustanowiść
     * połączenie
     *
     * @param evt
     */
    public void runFeedbackClient(String host) {
        System.out.println("runClient()");
        client = new SSLClient(host);

    }

    /**
     * Metoda zwracająca obiekt klasy SSLClient z list klientów
     *
     * @param evt
     */
    public SSLClient getClient(String clientIp) {
        SSLClient temp = (SSLClient) this.clientsMap.get(clientIp);
        return temp;
    }

    public SSLServer getServer() {
        return server;
    }

    /**
     * Metoda, która zatrzymuje wątek uruchominego SSLServera i
     * SSLSOcketConnection powodując zamknięcie działających modułów
     * architektury.
     *
     * @param evt
     */
    public void stopServer() {
        server.stopRunning();
    }
}
