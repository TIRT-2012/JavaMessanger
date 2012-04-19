/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Others.ApplicationComponents;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Trebronus
 */
public class CryptingFrame extends javax.swing.JFrame {    
    ArrayList<AlgoritmSettings> set;
    /**
     * Creates new form CryptingFrame
     */
    private ApplicationFrame applicationFrame = null;
    private ApplicationComponents applicationComponents = null;
    
    public CryptingFrame() {
        initComponents();
        initCrypto();
    }

    public CryptingFrame(ApplicationFrame applicationFrame) {
        initComponents();
        this.applicationFrame = applicationFrame;
        initCrypto();
    }
    
    public void initCrypto(){
        this.set = new ArrayList<AlgoritmSettings>();
        
        set.add(new AlgoritmSettings("AES", 128, 256, 64));
        set.add(new AlgoritmSettings("Blowfish", 32, 448, 8));
        set.add(new AlgoritmSettings("DES", 56, 56, 0));
        set.add(new AlgoritmSettings("DESede", 112, 168, 56));
        
        set.add(new AlgoritmSettings("Camellia", 128, 256, 64));
        set.add(new AlgoritmSettings("CAST5", 1, 128, 1));
        set.add(new AlgoritmSettings("CAST6", 1, 256, 1));
        set.add(new AlgoritmSettings("GOST28147", 256, 256, 0));
        
        set.add(new AlgoritmSettings("IDEA", 128, 128, 0));
        set.add(new AlgoritmSettings("Noekeon", 128, 128, 0));
        set.add(new AlgoritmSettings("RC2", 1, 512, 1));
        set.add(new AlgoritmSettings("RC5", 1, 128, 1));
        
        set.add(new AlgoritmSettings("RC6", 1, 256, 1));
        set.add(new AlgoritmSettings("SEED", 128, 128, 0));
        set.add(new AlgoritmSettings("Serpent", 128, 256, 64));
        set.add(new AlgoritmSettings("Skipjack", 1, 128, 1));
        
        set.add(new AlgoritmSettings("TEA", 128, 128, 0));
        set.add(new AlgoritmSettings("Twofish", 128, 256, 64));
        set.add(new AlgoritmSettings("XTEA", 128, 128, 0));
        
        this.algorithNameComboBox.setModel(this.getAlgoritmBoxModel());
        this.keySizeComboBox.setModel(this.getKeySizeBoxModel(0));
    }
    
    public ComboBoxModel getAlgoritmBoxModel(){
        String[] names = new String[this.set.size()];
        for(int i = 0; i < names.length; i++)
            names[i] = this.set.get(i).algoritmName;
        
        ComboBoxModel model = new DefaultComboBoxModel(names);
        return model;
    }
    
    public ComboBoxModel getKeySizeBoxModel(int algIndex){
        AlgoritmSettings settings = this.set.get(algIndex);
        ArrayList<String> keySizes = new ArrayList<String>();
        
        if(settings.minKeySize == settings.maxKeySize)
            keySizes.add(Integer.toString(settings.maxKeySize));
        else
            for(int i = settings.minKeySize; i <= settings.maxKeySize; i += settings.step)
                keySizes.add(Integer.toString(i));
        
        String[] algKeySizes = new String[keySizes.size()];
        
        for(int i = 0; i < algKeySizes.length; i++)
            algKeySizes[i] = keySizes.get(i);
        
        
        ComboBoxModel model = new DefaultComboBoxModel(algKeySizes);        
        return model;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        algorithNameComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cryptSettingsBtn = new javax.swing.JButton();
        keySizeComboBox = new javax.swing.JComboBox();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Wybierz algorytm szyfrowania");

        jLabel2.setText("Wybrany algorytm:");

        algorithNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithNameComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Długość klucza:");

        cryptSettingsBtn.setText("Zastosuj");
        cryptSettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cryptSettingsBtnActionPerformed(evt);
            }
        });

        keySizeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "56" }));
        keySizeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keySizeComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(algorithNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(keySizeComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cryptSettingsBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(algorithNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(keySizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(cryptSettingsBtn)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cryptSettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cryptSettingsBtnActionPerformed
        String algorithm = (String)algorithNameComboBox.getSelectedItem();
        int keySize = Integer.parseInt((String)keySizeComboBox.getSelectedItem());
        this.applicationComponents.getSSLController().setAlgorithm(algorithm);
        this.applicationComponents.getSSLController().setKeySize(keySize);
        this.setVisible(false);
    }//GEN-LAST:event_cryptSettingsBtnActionPerformed

    private void algorithNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algorithNameComboBoxActionPerformed
        int algIndex = this.algorithNameComboBox.getSelectedIndex();
        this.keySizeComboBox.setModel(this.getKeySizeBoxModel(algIndex));
    }//GEN-LAST:event_algorithNameComboBoxActionPerformed

    private void keySizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keySizeComboBoxActionPerformed

    }//GEN-LAST:event_keySizeComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(CryptingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CryptingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CryptingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CryptingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CryptingFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox algorithNameComboBox;
    private javax.swing.JButton cryptSettingsBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox keySizeComboBox;
    // End of variables declaration//GEN-END:variables

    public void setApplicationComponents(ApplicationComponents applicationComponents) {
        this.applicationComponents = applicationComponents;
    }
    
   public class AlgoritmSettings{
       String algoritmName;
       int minKeySize;
       int maxKeySize;
       int step;

        public AlgoritmSettings(String algoritmName, int minKeySize, int maxKeySize, int step) {
            this.algoritmName = algoritmName;
            this.minKeySize = minKeySize;
            this.maxKeySize = maxKeySize;
            this.step = step;
        }
   }
}