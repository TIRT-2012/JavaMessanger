/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocket;

/**
 *
 * @author SysOp
 */
public class SSLTestSocketC {

    static boolean SERVER = false;
    
    public static void main(String[] args) {
        if (SERVER) {
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
