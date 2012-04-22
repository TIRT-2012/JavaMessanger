/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketConference;

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
            SSLMultiClient c=new SSLMultiClient();
            c.prepare();
            c.run();
        }
    }
}
