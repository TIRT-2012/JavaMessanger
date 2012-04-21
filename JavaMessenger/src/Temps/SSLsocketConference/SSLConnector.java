/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocket;

/**
 *
 * @author Piotr
 */
public class SSLConnector {

    private boolean isServer = false;
    private String ip = null;

    public SSLConnector(boolean isClient) {
        isServer = (isClient) ? false : true;
    }
    
    public void setHostIp(String ip)
    {
        this.ip = ip;
    }
    
    public void run()
    {
        if (isServer) {
            SSLServer s = new SSLServer();
            Thread ts = new Thread(s);
            s.prepare();
            ts.start();
        }
        else{
            SSLClient c=new SSLClient();
            c.setIP(ip);
            c.prepare();
            c.run();
        }
    }
    
}
