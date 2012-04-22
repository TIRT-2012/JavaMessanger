/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.AudioStream;

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
        Processor audioProcessor=DeviceCapturer.getRealizedAudioProcessor();
        audioProcessor.start();

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
