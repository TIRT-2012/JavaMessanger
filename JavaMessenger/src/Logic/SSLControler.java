/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.UsersDAO;
import Entities.Users;
import GUI.MessegerFrame;
import Others.SSLClient;
import Others.SSLServer;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piotr
 */
public class SSLControler {

    private boolean isServer = false;
    private SSLServer server = null;
    private SSLClient client = null;
    private HashMap clientsMap = null;
    private Thread ts;
    private MessegerFrame msgr = null;

    public SSLControler() {
        runServer();
        clientsMap = new HashMap();
    }

    public String getUserName(String ip)
    {
        System.out.println("getUserName()");
        UsersDAO userDao = new UsersDAO();
        Users temp = userDao.findByIp(ip);
        return temp.getUserName();
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
            clientsMap.put(client.getHost(), client);
            client.run();

        }
    }

    public void runServer() {
        System.out.println("runServer()");
        setSSLConnection(false);
    }

    public void runClient() {
        System.out.println("runClient()");
        setSSLConnection(true);
    }

    public void quitServer() {
        System.out.println("SSLControler quitServer()");
        try {
            server.quit();
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void quitClient() {
        System.out.println("SSLControler quitClient()");
        try {
            server.quit();
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(SSLControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap getClientsMap() {
        return clientsMap;
    }

    public SSLClient getClient(String clientIp) {
        SSLClient temp = (SSLClient) this.clientsMap.get(clientIp);
        return temp;
    }

    public SSLServer getServer() {
        return server;
    }
}
