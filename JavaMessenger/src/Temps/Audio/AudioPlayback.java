/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Audio;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.format.AudioFormat;
import javax.media.protocol.DataSource;

/**
 *
 * @author SysOp
 */
public class AudioPlayback {

    public static void main(String[] args) {
        CaptureDeviceInfo di = null;

        Vector deviceList = CaptureDeviceManager.getDeviceList(
                new AudioFormat("linear"));
//        new AudioFormat("linear")
        if (deviceList.size() > 0) {
            di = (CaptureDeviceInfo) deviceList.get(0);
        }
        
        MediaLocator audioCapDevLoc=di.getLocator();
        
        DataSource ds=null;
        try {
            ds = Manager.createDataSource(audioCapDevLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Processor audioProcessor=null;
        
        try {
            audioProcessor = Manager.createProcessor(ds);
        } catch (NoProcessorException npe) {
            npe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        audioProcessor.realize();
        audioProcessor.prefetch();
        audioProcessor.start();
        
        try { //wait for realization
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(AudioPlayback.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataSource source = audioProcessor.getDataOutput();

        Player player=null;
        try {
            player=Manager.createPlayer(source);
        } catch (IOException ex) {
            Logger.getLogger(AudioPlayback.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPlayerException ex) {
            Logger.getLogger(AudioPlayback.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        player.realize();
        player.prefetch();
        player.start();

        
    }
}
