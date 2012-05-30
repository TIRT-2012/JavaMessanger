/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

/**
 *
 * @author Piotr
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.format.VideoFormat;
import javax.swing.JFrame;

public class VideoCaptureTest {

    CaptureDeviceInfo cam;
    MediaLocator locator;
    Player player;
    FormatControl formatControl;
    java.awt.Component videoComponent;
    String cameraType;
    Dimension cameraResolution;
    
    public VideoCaptureTest() {
        try {
            Dimension dm = new Dimension();
            dm.width = 640;
            dm.height = 480;
            VideoFormat format = new VideoFormat( "RGB", dm, 921600, Format.byteArray, 15.0f);
            //VideoFormat format = new VideoFormat(null);
            Vector deviceList = CaptureDeviceManager.getDeviceList(format);
            if (!deviceList.isEmpty()) {
                cam = (CaptureDeviceInfo) deviceList.firstElement();
                cameraType = cam.getName();
            }

//                        String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
//                        CaptureDeviceInfo temp = CaptureDeviceManager.getDevice(str2);
//                        cam = temp;
            System.out.println(" Pobrano camere.");
            locator = cam.getLocator();
            if (locator != null) {
                player = Manager.createRealizedPlayer(locator);
                if (player != null) {
                    player.start();
                    videoComponent = player.getVisualComponent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CaptureDeviceInfo getCam() {
        return cam;
    }

    public void setCam(CaptureDeviceInfo cam) {
        this.cam = cam;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Component getVideoComponent() {
        return videoComponent;
    }

    public void setVideoComponent(Component videoComponent) {
        this.videoComponent = videoComponent;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    
    
    
    public static void main(String[] args) {
        new VideoCaptureTest();
    }
}
