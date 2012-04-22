/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Audio;

import java.io.IOException;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;

/**
 *
 * @author SysOp
 */
public class AudioPlayer {
    public static void main(String[] args) throws IOException, NoPlayerException, CannotRealizeException{
        String url="rtp://5.132.15.195:49150/audio";
        Player player=Manager.createRealizedPlayer(new MediaLocator(url));
        player.start();
    }
}
