/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.SSLsocketFileTransmission;

/**
 *
 * @author SysOp
 */
public class SSLTestSocketC_1 {

    static boolean SERVER = false;

    public static void main(String[] args) {
        if (SERVER) {
            SSLServer_1 s = new SSLServer_1();
            Thread ts = new Thread(s);
            s.prepare();
            ts.start();
        }
        else{
            SSLClient_1 c=new SSLClient_1();
            c.prepare();
            c.run();
        }
    }
}
