/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Audio;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SysOp
 */
public class AudioConnectionTest {
    
    
    public static void main(String[] args){
        try {
            AudioConnection ac=new AudioConnection();
            ac.initialize(InetAddress.getByName("5.132.15.195"));
            ac.start();
            while(true){
                
            }
        } catch (Exception ex) {
            System.out.println("We have exception!");
            ex.printStackTrace();
        }
    }
}
