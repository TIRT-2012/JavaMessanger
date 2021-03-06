/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others.Video.Libraries;

/**
 *
 * @author Mateusz & SysOp
 */

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.*;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.SessionEvent;
import javax.media.rtp.rtcp.SourceDescription;

public class VideoConnection implements SessionListener, ReceiveStreamListener {

//    private static AudioConnection instance = new AudioConnection();
    private DataSource camera;
    private String cameraType;
    private Dimension cameraResolution;
    private CaptureDeviceInfo cam;
    private MediaLocator locator;
    private java.awt.Component videoComponent;
    private PushBufferDataSource rtpVideo;
    private RTPManager sessionManager;
    private SendStream outStream;
    private DataSource videoInputSource1;
    private int connectionPort = 5051;
    private Processor processor;
    private boolean started = false;
    private boolean initialized = false;
    private boolean isMuted = false;
    private boolean isPlayerMuted = false;
    private Player player;
    private SessionAnalizer sessionAnalizer;
    private byte[] des_key = {90, 32, 12, 74, 0, 23, 112};

    public VideoConnection() {
        camera = getCamera();
        processor = getProcessor(camera);
        rtpVideo = (PushBufferDataSource) processor.getDataOutput();
    }

//    public static AudioConnection getInstance() {
//        return instance;
//    }
    public void initialize(InetAddress toIp) throws Exception {
        if (!initialized) {
            try {
                sessionManager = RTPManager.newInstance();
                SessionAddress localAddr = new SessionAddress(InetAddress.getByName(InetAddress.getLocalHost().getHostName()), connectionPort);

                //Stary kod inicjalizacji
                //sessionManager.initialize(localAddr);
                //Nowy kod inicjalizacji, dodający szyfrowanie
                String cname = SourceDescription.generateCNAME();
                SourceDescription sourceDescription[] = {
                    new SourceDescription(3, "jmf-user@sun.com", 1, false), new SourceDescription(1, cname, 1, false), new SourceDescription(6, "JMF RTP Player v2.0", 1, false)
                };
                double rtcp_bw_fraction = 0.050000000000000003D;
                double rtcp_sender_bw_fraction = 0.25D;

                SessionAddress localAddresses[] = {localAddr};

                sessionManager.initialize(localAddresses, sourceDescription, rtcp_bw_fraction, rtcp_sender_bw_fraction, new EncryptionInfo(EncryptionInfo.DES, des_key));
                //Koniec

//                for(Contact contact: conversation.getContactArray()){
//                        SessionAddress remoteAddr = new SessionAddress(InetAddress.getByName(contact.getIpAddress()), connectionPort);
                SessionAddress remoteAddr = new SessionAddress(toIp, connectionPort);
                sessionManager.addTarget(remoteAddr);
//                }


                outStream = sessionManager.createSendStream(rtpVideo, 0);

                sessionManager.addSessionListener(this);
                sessionManager.addReceiveStreamListener(this);

                initialized = true;

            } catch (UnsupportedFormatException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidSessionAddressException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new Exception("VideoConnection is already initialized. Must be closed first.");
        }
    }

    public void start() {
        if (initialized) {
            try {
                outStream.start();
                unmuteInput();
                if (!isPlayerMuted && player != null) {
                    player.start();
                }
                started = true;
                System.out.println("Video started");
            } catch (IOException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
//            sessionAnalizer = new SessionAnalizer();
//            sessionAnalizer.start();
        }
    }

    public void stop() {
        if (started) {
            try {
                outStream.stop();
                muteInput();
                if (player != null) {
                    player.stop();
                }
                started = false;
            } catch (IOException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        sessionAnalizer.interrupt();
    }

    public void close() {
        if (initialized) {
            if (player != null) {
                player.close();
                player = null;
            }
            outStream.close();
            outStream = null;
            sessionManager.dispose();
            sessionManager = null;
            initialized = false;
            started = false;
            unmuteInput();
            unmuteOutput();
        }
    }

    public void muteInput() {
        processor.stop();
        isMuted = true;
    }

    public void unmuteInput() {
        processor.start();
        isMuted = false;
    }

    public void muteOutput() {
        isPlayerMuted = true;
        if (player != null) {
            player.stop();
        }
    }

    public void unmuteOutput() {
        isPlayerMuted = false;
        if (player != null) {
            player.start();
        }
    }

    public boolean isStarted() {
        return started;
    }

    public void update(SessionEvent se) {
        System.out.println(se);
    }

    public synchronized void update(ReceiveStreamEvent evt) {
//        System.out.println(evt);
        if (evt instanceof NewReceiveStreamEvent) {

            try {
                ReceiveStream stream = ((NewReceiveStreamEvent) evt).getReceiveStream();
                player = Manager.createRealizedPlayer(stream.getDataSource());
                if (!isPlayerMuted) {
                    player.start();
                    videoComponent = player.getVisualComponent();
                }
            } catch (CannotRealizeException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoPlayerException ex) {
                Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //=============================PRIVATE METHODS==============================
    private DataSource getCamera(){
//        Dimension dm = new Dimension();
//        dm.width = 640;
//        dm.height = 480;
//        VideoFormat format = new VideoFormat("RGB", dm, 921600, Format.byteArray, 15.0f);
        VideoFormat format = new VideoFormat(null);
        Vector deviceList = CaptureDeviceManager.getDeviceList(format);
        if (!deviceList.isEmpty()) {
            cam = (CaptureDeviceInfo) deviceList.firstElement();
            cameraType = cam.getName();
            locator = cam.getLocator();
            if (locator != null) {
                try {
                    return Manager.createDataSource(locator);
                } catch (IOException ex) {
                    Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataSourceException ex) {
                    Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private Processor getProcessor(DataSource videoSource) {
        try {
//            Dimension dm = new Dimension();
//            dm.width = 640;
//            dm.height = 480;
//            VideoFormat[] formats = {new VideoFormat("RGB", dm, 921600, Format.byteArray, 15.0f)};
            VideoFormat[] formats = {new VideoFormat(null)};
            ContentDescriptor descriptor = new ContentDescriptor(ContentDescriptor.RAW_RTP);
            Processor processor = Manager.createRealizedProcessor(new ProcessorModel(videoSource, formats, descriptor));

            return processor;
        } catch (IOException ex) {
            Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoProcessorException ex) {
            Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotRealizeException ex) {
            Logger.getLogger(VideoConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private class SessionAnalizer extends Thread {

        public SessionAnalizer() {
        }

        public void run() {
            while (!interrupted()) {
            }
        }
    }
}
