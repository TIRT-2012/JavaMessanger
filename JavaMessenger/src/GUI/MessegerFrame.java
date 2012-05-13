/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Logic.SSLController;
import Others.ApplicationComponents;
import Others.SSLClient;
import Others.SSLSocketConnection;
import Temps.Audio.AudioConnection;
import crypto.JCECrypter;
import crypto.SerialCryptedMessage;
import crypto.SerialPublicKey;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Piotr
 */
public class MessegerFrame extends javax.swing.JFrame {

    private ConferenceFrame conference;
    private boolean isServer = false;
    private boolean prepareToCloseThread = false;
    private SSLController sslControler = null;
    private String profilName = null;
    private String hostIp = null;
    private SSLClient client = null;
    private SSLSocketConnection sslSocketConnection = null;
    private String myProfilName = null;
    private PublicKey publicKey = null;
    private String algorithm = null;
    private int symetricKeySize;
    private FileSender fileSender = null;
    private OutputStream output = null;

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getSymetricKeySize() {
        return symetricKeySize;
    }

    public void setSymetricKeySize(int symetricKeySize) {
        this.symetricKeySize = symetricKeySize;
    }

    public JTextArea getjTextArea1() {
        return jTextArea1;
    }

    public void setjTextArea1(JTextArea jTextArea1) {
        this.jTextArea1 = jTextArea1;
    }

    public String getIp() {
        return hostIp;
    }

    public void setIp(String ip) {
        this.hostIp = ip;
    }

    public SSLController getSslControler() {
        return sslControler;
    }

    public void setSslControler(SSLController sslControler) {
        this.sslControler = sslControler;
    }

    /**
     * Creates new form Messeger
     */
    public MessegerFrame() {
        initComponents();
        this.addWindowListener(listener);
        System.out.println("MessegerFrame()");
    }

    public MessegerFrame(SSLController sslControler) {
        initComponents();
        this.addWindowListener(listener);
        this.sslControler = sslControler;
        myProfilName = (String) sslControler.getApplicationComponents().getLoginController().getUserObject().getUserName();
    }

    public MessegerFrame(ConferenceFrame conf) {
        initComponents();

        this.conference = conf;
    }

    public void sendFile(FileSender fileSender) throws IOException {
        this.fileSender = fileSender;

        String url = fileSender.getjTextField1().getText();
        System.out.println(url);
        File myFile = new File(url);
        
        String message = "<<%file%>>"+myFile.getName();
        System.out.println(message);
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JCECrypter jce = new JCECrypter(this.getSslControler().getAlgorithm(), this.getSslControler().getKeySize());
        SerialCryptedMessage sCm = null;
        try {
            sCm = jce.cryptOut(publicKey, in, out);
        } catch (Exception ex) {
            Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Zaszyfrowana wiadomość: " + out.toString());
        String cryptedMessage = out.toString();
        System.out.println("CryptedMessage: " + cryptedMessage);

        this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getObjectOutputStream().writeObject(sCm);
        sCm = null;
        
        byte[] byteArray = new byte[(int) myFile.length()];
        
        byteArray = convertToByte(byteArray, myFile);
        ByteArrayInputStream in2 = new ByteArrayInputStream(byteArray);
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        try {
            System.out.println("Moj klucz publiczny do szyfrowania wiadomosci" + publicKey);
            sCm = jce.cryptOut(publicKey, in2, out2);
        } catch (Exception ex) {
            Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Zaszyfrowana wiadomość: " + out2.toString());
        System.out.println("CryptedMessage: " + cryptedMessage);
        try {
            this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getObjectOutputStream().writeObject(sCm);
        } catch (IOException ex) {
            Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static byte[] getBytes(Object obj) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        byte[] data = bos.toByteArray();
        return data;
    }
    public byte[] convertToByte(byte[] byteArray, File file) throws FileNotFoundException, IOException
    {
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArray);
        return byteArray;
    }

    public void addSSLSocketConnection(SSLSocketConnection sslSocketConnection) {
        this.sslSocketConnection = sslSocketConnection;
    }

    public SSLSocketConnection getSSLSocketConnection(SSLSocketConnection sslSocketConnection) {
        return this.sslSocketConnection;
    }

    public void addSSLClient(SSLClient sllClient) {
        this.client = sllClient;
    }

    public SSLClient getSSLClient() {
        return this.client;
    }

    public void setSSLControler(SSLController sslControler) {
        this.sslControler = sslControler;
    }

    public void setMessage(String message) {
        this.jTextArea1.append(message + "\n");
        ///this.jTextArea1.repaint(); // na wszelki wypadek
    }

    public String getMessage() {

        return this.jTextArea2.getText();
    }

    public void runServer() {
        System.out.println("runServer()");
        sslControler.setSSLConnection(false);
    }

    public void runClient() {
        System.out.println("runClient()");
        sslControler.setSSLConnection(true);
    }

//    public void closeClient() {
//        System.out.println("closeClient()");
//        sslControler.quitClient();
//
//    }

    public void changeJLabel1(String profilName) {
        this.profilName = profilName;
        this.jLabel1.setText("Rozmowa z użytkownikiem: " + profilName);
    }

    public void updateProfilName() {

        profilName = this.sslControler.getUserName(hostIp);
    }

    public String getProfilName() {
        return profilName;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        audioConferenceButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        manageConference = new javax.swing.JMenuItem();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea2KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("Wyśłij");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Kasuj");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Rozmowa z:");

        jButton3.setText("Wyślij plik");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        audioConferenceButton.setText("Rozmowa audio");
        audioConferenceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                audioConferenceButtonMouseClicked(evt);
            }
        });

        jMenu1.setText("Program");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Profil");

        manageConference.setText("Zarządzaj konferencją");
        manageConference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageConferenceActionPerformed(evt);
            }
        });
        jMenu2.add(manageConference);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(audioConferenceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 266, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(4, 4, 4)
                        .addComponent(jButton2))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(audioConferenceButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void manageConferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageConferenceActionPerformed
        if (conference == null) {
            JOptionPane.showMessageDialog(this, "Aby rozpocząc konferencję, musisz się wpierw zalogować");
        } else {
            conference.modifyConference();
        }

    }//GEN-LAST:event_manageConferenceActionPerformed

    private void jTextArea2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.setKeyCode(8);
            String message = this.getMessage();
            try {
                //////crypting
                ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JCECrypter jce = new JCECrypter(getSslControler().getAlgorithm(), getSslControler().getKeySize());
                SerialCryptedMessage sCm = null;
                try {
                    System.out.println("Moj klucz publiczny do szyfrowania wiadomosci" + publicKey);
                    sCm = jce.cryptOut(publicKey, in, out);
                } catch (Exception ex) {
                    Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Zaszyfrowana wiadomość: " + out.toString());
                String cryptedMessage = out.toString();
                System.out.println("CryptedMessage: " + cryptedMessage);

                this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getObjectOutputStream().writeObject(sCm);

                /////////////
                //this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getStreamOut().writeUTF(cryptedMessage); // zaciągam strumień clienta o 
                System.out.println(" message sent ");
                String temp = "Connection with " + hostIp + " , (JA) " + myProfilName;
                this.setMessage(temp);
                this.setMessage(message);
                jTextArea2.setText("");
                jTextArea2.setCaretPosition(0);
                jTextArea2.requestFocus();
            } catch (IOException ex) {
                Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextArea2KeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        String message = this.getMessage();
        try {
            //////crypting
            ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JCECrypter jce = new JCECrypter(getSslControler().getAlgorithm(), getSslControler().getKeySize());
            SerialCryptedMessage sCm = null;
            try {
                System.out.println("Moj klucz publiczny do szyfrowania wiadomosci" + publicKey);
                sCm = jce.cryptOut(publicKey, in, out);
            } catch (Exception ex) {
                Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Zaszyfrowana wiadomość: " + out.toString());
            String cryptedMessage = out.toString();
            System.out.println("CryptedMessage: " + cryptedMessage);

            this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getObjectOutputStream().writeObject(sCm);

            /////////////
            //this.sslControler.getServer().getFrameFromMap(hostIp).getSSLClient().getStreamOut().writeUTF(message);
            System.out.println(" message sent ");
            String temp = "Connection with " + hostIp + " , (JA) " + myProfilName;
            this.setMessage(temp);
            this.setMessage(message);
            jTextArea2.setText("");
            jTextArea2.setCaretPosition(0);
            jTextArea2.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(MessegerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        FileSender fs = new FileSender();
        fs.setMessengerFrame(this);
        fs.setLocationRelativeTo(fs.getRootPane());
        fs.setVisible(true);
    }//GEN-LAST:event_jButton3MouseClicked

    private void audioConferenceButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioConferenceButtonMouseClicked
        // TODO add your handling code here:
        if(sslSocketConnection!=null){
            InetAddress toIp=sslSocketConnection.getIp();
            AudioConnection audioConnection=ApplicationComponents.getInstance().getAudioConnection();
            try{
                audioConnection.initialize(toIp);
                audioConnection.start();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }//GEN-LAST:event_audioConferenceButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MessegerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MessegerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MessegerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MessegerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MessegerFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton audioConferenceButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JMenuItem manageConference;
    // End of variables declaration//GEN-END:variables
    WindowListener listener = new WindowAdapter() {

        public void windowClosing(WindowEvent w) {
            System.out.println("123 zamykamy");
////            wtf?
////            closeClient();
//
        }
    };
}
