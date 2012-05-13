/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps.Audio;

/**
 *
 * @author Mateusz & SysOp
 */
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.*;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.SessionEvent;
import javax.media.rtp.rtcp.SourceDescription;

public class AudioConnection implements SessionListener, ReceiveStreamListener{
    
    private static AudioConnection instance = new AudioConnection();
    
    private DataSource microphone;
    private PushBufferDataSource  rtpSound;
    private RTPManager sessionManager;
    private SendStream  outStream;
    private DataSource audioInputSource1;
    private int connectionPort = 5051;
    private Processor processor;
    
    private boolean started=false;
    private boolean initialized = false;
    
    private boolean isMuted=false;
    private boolean isPlayerMuted=false;
    private Player player;
    
    private SessionAnalizer sessionAnalizer;

    private AudioConnection() {
        microphone = getMicrophone();
        processor = getProcessor(microphone);
        rtpSound = (PushBufferDataSource) processor.getDataOutput();
    }

    public static AudioConnection getInstance() {
        return instance;
    }

    public void initialize(InetAddress toIp) throws Exception {
        if(!initialized){
            try {
                sessionManager = RTPManager.newInstance();
                SessionAddress localAddr= new SessionAddress( InetAddress.getByName(InetAddress.getLocalHost().getHostName()), connectionPort);
                sessionManager.initialize(localAddr);

//                for(Contact contact: conversation.getContactArray()){
//                        SessionAddress remoteAddr = new SessionAddress(InetAddress.getByName(contact.getIpAddress()), connectionPort);
                    SessionAddress remoteAddr = new SessionAddress(toIp,connectionPort);    
                    sessionManager.addTarget(remoteAddr);
//                }


                outStream = sessionManager.createSendStream(rtpSound, 0);

                sessionManager.addSessionListener(this);
                sessionManager.addReceiveStreamListener(this);

                initialized = true;

            } catch (UnsupportedFormatException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidSessionAddressException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            throw new Exception("AudioConnection is already initialized. Must be closed first.");
        }
    }

    public void start(){
        if(initialized){
            try {
                outStream.start();
                unmuteInput();
                if(!isPlayerMuted && player != null){
                    player.start();
                }
                started = true;
                System.out.println("Audio started");
            } catch (IOException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
//            sessionAnalizer = new SessionAnalizer();
//            sessionAnalizer.start();
        }
    }
    
    public void stop(){
        if(started){
            try {
                outStream.stop();
                muteInput();
                if(player != null){
                    player.stop();
                }
                started = false;
            } catch (IOException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        sessionAnalizer.interrupt();
    }
    
    public void close(){
        if(initialized){
            if(player != null){
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

    public void muteInput(){
        processor.stop();
        isMuted=true;
    }
    
    public void unmuteInput(){
        processor.start();
        isMuted=false;
    }
    
    public void muteOutput(){
        isPlayerMuted=true;
        if(player!=null)
            player.stop();
    }
    
    public void unmuteOutput(){
        isPlayerMuted=false;
        if(player!=null)
            player.start();
    }
    
    public boolean isStarted(){
        return started;
    }
    
    public void update(SessionEvent se) {
        System.out.println(se);
    }

    public synchronized void update(ReceiveStreamEvent evt) {
//        System.out.println(evt);
        if(evt instanceof NewReceiveStreamEvent){

            try {
                ReceiveStream stream = ((NewReceiveStreamEvent)evt).getReceiveStream();
                player = Manager.createRealizedPlayer(stream.getDataSource());
                if(!isPlayerMuted)
                    player.start();
            } catch (CannotRealizeException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoPlayerException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //=============================PRIVATE METHODS==============================
        
    private DataSource getMicrophone(){
        AudioFormat format = new AudioFormat(AudioFormat.LINEAR, 44100, 16, 2);
        Vector deviceList = CaptureDeviceManager.getDeviceList(format);

        if(!deviceList.isEmpty()){
            try {
                CaptureDeviceInfo info = (CaptureDeviceInfo)deviceList.firstElement();
                return Manager.createDataSource(info.getLocator());
            } catch (IOException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoDataSourceException ex) {
                Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    private Processor getProcessor(DataSource soundSource){
        try {
            AudioFormat[] formats = {new AudioFormat(AudioFormat.MPEG_RTP, 44100, 16, 2)};
            ContentDescriptor descriptor = new ContentDescriptor(ContentDescriptor.RAW_RTP);
            Processor processor = Manager.createRealizedProcessor(new ProcessorModel(soundSource, formats, descriptor));
         
            return processor;
        } catch (IOException ex) {
            Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoProcessorException ex) {
            Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotRealizeException ex) {
            Logger.getLogger(AudioConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private class SessionAnalizer extends Thread{

        public SessionAnalizer() {}
        
        public void run(){
            while(!interrupted()){
                
            }
        }
        
    }
