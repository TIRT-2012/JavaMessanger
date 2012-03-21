/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

/**
 *
 * @author SysOp
 */
public class TestSocketC {

    static boolean SERVER = false;

    public static void main(String[] args) {
        if (SERVER) {
            Server s = new Server();
            Thread ts = new Thread(s);
            s.prepare();
            ts.start();
        }
        else{
            Client c=new Client();
            c.prepare();
            c.run();
        }
    }
}
