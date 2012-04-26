package Temps.Audio;

/*
 * ChatFrame.java	21/06/07
 * author: Max
 * MSN: zengfc@21cn.com
 * QQ: 22291911
 * Email: zengfc@21cn.com
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.SocketException;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;

public class ChatFrame extends JFrame{
    ChatCapture cap = null;
    TraAndRec tar = null;
    boolean isVideo = false;

    String name;
    boolean hasServer;
    String serverAddr;
    int serverPort;

    JPanel contentPane;
    JPanel setupPanel = new JPanel();
    JPanel GeneralPanel = new JPanel();
    JPanel videoPanel = new JPanel();
    JLabel statusBar = new JLabel();
    JLabel addressLabel = new JLabel();
    JLabel audioQualityLabel = new JLabel();
    JLabel videoQualityLabel = new JLabel();
    JTabbedPane jTabbedPane1 = new JTabbedPane();
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout4 = new BorderLayout();
    JButton connectButton = new JButton();
    JButton disconnectButton = new JButton();
    JButton videoButton = new JButton();
    JTextField addressTextField = new JTextField();
    JComboBox audioQualityComboBox = new JComboBox();
    JComboBox videoQualityComboBox = new JComboBox();
    JScrollPane jScrollPane1 = new JScrollPane();
    String[] head = {"Name", "IP Address", "Ctrl Port", "Audio Port", "Audio RTP Port", "Video Port", "Video RTP Port"};
    DefaultTableModel defaultModel = new DefaultTableModel(head, 0);
    JTable msgTable = new JTable(defaultModel);
    JButton multiButton = new JButton();
    JLabel portLabel = new JLabel();
    JTextField portTextField = new JTextField();
    JLabel ttlLabel = new JLabel();
    JTextField ttlTextField = new JTextField();

    public ChatFrame(String name, boolean hasServer, String serverAddr, int serverPort) {
        this.name = name;
        this.hasServer = hasServer;
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
        try {
            jbInit();
            setVisible(true);
            addWindowListener(
                    new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    tar.sayBye();//���û�û�е��Ͽ�����ť������ֱ�ӹش���ʱ�����ô˺���
                    System.exit(0);
                }
            }
            );
            RTPInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        setSize(new Dimension(250, 466));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation((screenSize.width - frameSize.width) / 2,
                    (screenSize.height - frameSize.height) / 2);
        setTitle("�������");
        setLocale(new java.util.Locale("zh", "CN", ""));
        setResizable(false);
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        statusBar.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        statusBar.setText(" �ȴ���...");
        contentPane.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        contentPane.setBorder(null);
        GeneralPanel.setLayout(null);
        GeneralPanel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        GeneralPanel.setBorder(null);
        GeneralPanel.setDebugGraphicsOptions(0);
        GeneralPanel.setToolTipText("");
        connectButton.setBounds(new Rectangle(22, 325, 86, 23));
        connectButton.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        connectButton.setText("l��");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectButton_actionPerformed(e);//���û�����l�Ӱ�ťʱ�����ô˺���
            }
        });
        disconnectButton.setBounds(new Rectangle(22, 354, 86, 23));
        disconnectButton.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        disconnectButton.setText("�Ͽ�");
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dissconnectButton_actionPerformed(e);//���û����¶Ͽ���ťʱ�����ô˺���
            }
        });
        jTabbedPane1.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        addressTextField.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        addressTextField.setBorder(BorderFactory.createEtchedBorder());
        addressTextField.setText("234.55.66.88");
        addressTextField.setBounds(new Rectangle(83, 23, 129, 23));
        addressLabel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        addressLabel.setText("�鲥��ַ��");
        addressLabel.setBounds(new Rectangle(2, 23, 68, 23));
        audioQualityLabel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        audioQualityLabel.setText("��Ƶ��ʽ��");
        audioQualityLabel.setBounds(new Rectangle(2, 138, 65, 14));
        audioQualityComboBox.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        audioQualityComboBox.setBorder(null);
        audioQualityComboBox.setBounds(new Rectangle(2, 162, 235, 22));
        audioQualityComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                audioQualityComboBox_actionPerformed(e);//ѡ����Ƶ��ʽ�������ز���
            }
        });
        jScrollPane1.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.black));
        jScrollPane1.setBounds(new Rectangle(5, 195, 227, 122));
        msgTable.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        msgTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setupPanel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        multiButton.setBounds(new Rectangle(128, 325, 86, 23));
        multiButton.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        multiButton.setText("��������");
        multiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                multiButton_actionPerformed(e);//ѡ�񡰶������족�����ز���
            }
        });
        portLabel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        portLabel.setText("�˿ڣ�");
        portLabel.setBounds(new Rectangle(2, 61, 40, 23));
        portTextField.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        portTextField.setBorder(BorderFactory.createEtchedBorder());
        portTextField.setText("6666");
        portTextField.setBounds(new Rectangle(83, 61, 129, 23));
        ttlLabel.setFont(new java.awt.Font("����", Font.PLAIN, 13));
        ttlLabel.setText("TTL:");
        ttlLabel.setBounds(new Rectangle(2, 103, 34, 14));
        ttlTextField.setBorder(BorderFactory.createEtchedBorder());
        ttlTextField.setText("2");
        ttlTextField.setBounds(new Rectangle(83, 98, 129, 23));
        contentPane.add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        contentPane.add(statusBar, java.awt.BorderLayout.SOUTH);
        GeneralPanel.add(videoPanel);
        GeneralPanel.add(jScrollPane1);
        GeneralPanel.add(multiButton);
        GeneralPanel.add(connectButton);
        GeneralPanel.add(disconnectButton);
        GeneralPanel.add(videoButton);
        jScrollPane1.getViewport().add(msgTable);
        jTabbedPane1.add(GeneralPanel, "����",0);
        setupPanel.setLayout(null);
        setupPanel.add(portTextField);
        setupPanel.add(addressTextField);
        setupPanel.add(portLabel);
        setupPanel.add(addressLabel);
        setupPanel.add(ttlTextField);
        setupPanel.add(ttlLabel);
        setupPanel.add(audioQualityComboBox);
        setupPanel.add(videoQualityLabel);
        setupPanel.add(videoQualityComboBox);
        setupPanel.add(audioQualityLabel);
        jTabbedPane1.add(setupPanel, "����", 1);
        jTabbedPane1.setSelectedIndex(0);
    }

    public void RTPInit() throws SocketException {

        cap = new ChatCapture();//����豸���ɼ�ԭʼ����Ƶ

        tar = new TraAndRec(cap.audioCapDevLoc, name, hasServer, serverAddr, serverPort, this);
        tar.initAudioProcessor();
    }

    public void connectButton_actionPerformed(ActionEvent e) {
        int row = msgTable.getSelectedRow();
        if(row != -1){
            String s[] = new String[7];

            for (int i = 0; i < defaultModel.getColumnCount(); i++)
                s[i] = (String) defaultModel.getValueAt(row, i);

            tar.setDestAddressAndPort(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]));

            Object value = msgTable.getValueAt(0, 0);
            if (name.equals(value)) {//���еĵ�һ���Ǳ������Ϣ
                for (int i = 0; i < defaultModel.getColumnCount(); i++)
                    s[i] = (String) defaultModel.getValueAt(0, i);

                String msg = s[1] + ":" + s[2] + ":" + s[3] + ":" + s[4] + ":" +
                             s[5] + ":" + s[6];
                tar.needConnect(msg);
            }
            else JOptionPane.showMessageDialog(null, "����: �Ҳ����������Ӧ�ĵ�ַ���˿�\n���������");
        }
    }

    public void dissconnectButton_actionPerformed(ActionEvent e) {
        tar.closeTransmit();
        videoPanel.removeAll();
        isVideo = false;

        connectButton.setEnabled(true);
        multiButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        videoButton.setEnabled(false);
        tar.isMulticastAddress = false;
        statusBar.setText(" �ȴ���...");
    }

    public void multiButton_actionPerformed(ActionEvent e) {
        String address = addressTextField.getText().trim();
        int port = Integer.valueOf(portTextField.getText().trim()).intValue();
        int ttl = Integer.valueOf(ttlTextField.getText().trim()).intValue();
        tar.setMulticastAndTTL(ttl);
        tar.setDestAddressAndPort(address, port, port, port);
        rtpConnect(addressTextField.getText());
    }

    public void rtpConnect(String destAddress){
        String result = tar.startAudioTransmit();//��ʼ��RTP����������Ƶ
        if (result != null) {
            JOptionPane.showMessageDialog(null,
                                          "����: " + result);
        }
        connectButton.setEnabled(false);
        multiButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        videoButton.setEnabled(true);

        statusBar.setText(" �� " + destAddress + " ͨ����...");
    }

    public void audioQualityComboBox_actionPerformed(ActionEvent e) {
        tar.setAudioFormat(audioQualityComboBox.getSelectedIndex());
    }

    public int findTableRow(String name, String address){

        int rowCount = msgTable.getRowCount();
        int columnCount = msgTable.getColumnCount();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {

                Object value = msgTable.getValueAt(i, j);
                if (name.equals(value)) {

                    for(int k = 0; k < columnCount; k++){
                        value = msgTable.getValueAt(i, k);
                        if (address.equals(value)){
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public String getLocalAddress(){

        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
        }
        return addr.getHostAddress();
    }

}
