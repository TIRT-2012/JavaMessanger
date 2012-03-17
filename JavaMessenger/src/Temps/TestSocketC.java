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

    static boolean SERVER = true;

    public static void main(String[] args) {
        if (SERVER) {
            Server s = new Server();
            s.prepare();
            s.run();
        }
        else{
            Client c=new Client();
            c.prepare();
            c.run();
        }
    }
}
