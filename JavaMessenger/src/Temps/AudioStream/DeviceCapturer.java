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
public class DeviceCapturer {

    public static Processor getAudioProcessor() {
        CaptureDeviceInfo di = null;
        Vector deviceList = CaptureDeviceManager.getDeviceList(
                new AudioFormat("linear"));
        if (deviceList.size() > 0) {
            di = (CaptureDeviceInfo) deviceList.get(0);
        }
        MediaLocator audioCapDevLoc = di.getLocator();
        DataSource ds = null;
        try {
            ds = (DataSource) Manager.createDataSource(audioCapDevLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Processor audioProcessor = null;
        try {
            audioProcessor = Manager.createProcessor(ds);
        } catch (NoProcessorException npe) {
            npe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return audioProcessor;
    }

    public static Processor getRealizedAudioProcessor() {
        final Processor audioProcessor = getAudioProcessor();
        audioProcessor.realize();
        while (audioProcessor.getState() != Controller.Realized) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(DeviceCapturer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return audioProcessor;
    }
}
