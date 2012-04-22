/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Audio;

import javax.media.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

/**
 *
 * @author SysOp
 */
public class AudioTransmiter {

    public static void main(String[] args) throws MalformedURLException, IOException, NoDataSourceException, NoDataSinkException, NoProcessorException, CannotRealizeException {
        MediaLocator mediaLocator = null;
        DataSink dataSink = null;
        Processor mediaProcessor = null;
        String some = "rtp://5.126.123.85:49150/audio";
        Format[] FORMATS = new Format[]{new AudioFormat(AudioFormat.LINEAR)};
        ContentDescriptor CONTENT_DESCRIPTOR = new ContentDescriptor(ContentDescriptor.RAW_RTP);
        CaptureDeviceInfo di = null;
        Vector deviceList = CaptureDeviceManager.getDeviceList(
                new AudioFormat("linear"));
        if (deviceList.size()
                > 0) {
            di = (CaptureDeviceInfo) deviceList.get(0);
        }
        MediaLocator audioCapDevLoc = di.getLocator();
        DataSource ds = null;
        try {
            ds = Manager.createDataSource(audioCapDevLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaLocator = new MediaLocator(some);
        mediaProcessor = Manager.createRealizedProcessor(new ProcessorModel(ds, FORMATS, CONTENT_DESCRIPTOR));
        mediaProcessor.start();

        dataSink = Manager.createDataSink(mediaProcessor.getDataOutput(), mediaLocator);
        dataSink.open();
        dataSink.start();
    }
}
