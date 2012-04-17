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

    public String getUserName(String ip) {
        System.out.println("getUserName()");
        UsersDAO userDao = applicationComponents.getUsersDAO();
        Users temp = userDao.findByIp(ip, true);
        applicationComponents.releseDAO(userDao);
        return (String) temp.getUserName();
    }

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

    public final void runServer() {
        System.out.println("runServer()");
        setSSLConnection(false);
    }

    public void runClient(String ip) {
        System.out.println("runClient()");
        client = new SSLClient();
        client.setHost(ip);
        client.prepare();
        client.run();
        clientsMap.put(ip, client);
    }

    public void runFeedbackClient(String host) {
        System.out.println("runClient()");
        client = new SSLClient(host);

    }

    public void quitServer() {
        System.out.println("SSLControler quitServer()");
        try {
            server.quit();
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void quitClient() {
        System.out.println("SSLControler quitClient()");
        try {
            server.quit();
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SSLClient getClient(String clientIp) {
        SSLClient temp = (SSLClient) this.clientsMap.get(clientIp);
        return temp;
    }

    public SSLServer getServer() {
        return server;
    }
}
