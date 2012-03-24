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

    public SSLConnector(boolean isClient) {
        isServer = (isClient) ? false : true;
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
            c.prepare();
            c.run();
        }
    }
}
