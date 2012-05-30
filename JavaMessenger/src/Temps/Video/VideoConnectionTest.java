/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Video;

import java.net.InetAddress;

/**
 *
 * @author Piotrek
 */
public class VideoConnectionTest {
    public static void main(String[] args){
        try {
            VideoConnection ac=new VideoConnection();
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
