package Temps.Audio;

/*
 * ChatServer.java	21/06/07
 * author: Max
 * MSN: zengfc@21cn.com
 * QQ: 22291911
 * Email: zengfc@21cn.com
 *
 */

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {

    DatagramSocket ds;

    byte[] recbuf = new byte[1024];

    DatagramPacket rec = new DatagramPacket(recbuf, recbuf.length);

    int port;

    String newAddress = "";
    String addressList = "";
    String sendAddressList = "";

    public ChatServer(int port) {
        this.port = port;
    }

    public void init() throws Exception {//��ʼ��DatagramSocket
        if (port < 1024 || port > 65535) {
            System.out.println("�Զ���ķ���˿ںŴ���ϵͳ�Զ�ָ���˿�Ϊ��2008����");
            ds = new DatagramSocket(2008);
        } else {
            ds = new DatagramSocket(port);
        }
    }

    public void start() throws Exception {
        println("��������ʼ����");
        println("����˿�:" + port);
        init();
        receive();
    }

    public void receive() {
        for ( ; ; ) {
            try {
                ds.receive(rec);

                String msg = new String(rec.getData(), rec.getOffset(), rec.getLength());
                String natAddress = rec.getAddress().toString().substring(1);
                int natPort = rec.getPort();

                String Num = msg.substring(0,1);
                msg = msg.substring(2);
                String line;

                switch(Integer.parseInt(Num)){//1��ʾ����UDP��2��ʾ��ƵUDP��3��ʾ��ƵRTCP UDP��4��ʾ��ƵUDP��5��ʾ��ƵRTCP UDP��6��ʾ�뿪��7��ʾ��������
                    case 1 : {
                        line = "һ���µ��û���¼: " + msg + "  NAT��ַ: " + natAddress + " ����˿�: " + natPort;
                        sendAddressList = sendAddressList + natAddress + " " + natPort + " ";
                        //sendAddressList�����û������࣬���ϵļӳ�
                        newAddress = msg + ":" + natAddress + ":" + natPort;
                        //newAddress��ÿ���û�����Ϣ��ÿ����һ������ϴεĸ��ǡ�
                        println(line);
                        break;
                    }

                    case 2 : {
                        line = "��Ƶ�˿�: " + natPort;
                        newAddress = newAddress + ":" + natPort;
                        println(line);
                        break;
                    }

                    case 3 : {
                        line = "��ƵRTCP�˿�: " + natPort;
                        newAddress = newAddress + ":" + natPort;

                        println(line);
                        break;

                    }

                    case 4 : {
                        line = "��Ƶ�˿�: " + natPort;
                        newAddress = newAddress + ":" + natPort;

                        println(line);
                        break;
                    }

                    case 5 : {
                        line = "��ƵRTCP�˿�: " + natPort;
                        newAddress = newAddress + ":" + natPort;
                        println(line);
                        if (!sendAddressList.equals(""))
                            doSend("1 " + newAddress, sendAddressList);
                            //��仰�ǰѸղŷ���Ϣע����Ǹ��û��Ĺ����ַ���˿ڷ������������û���sendAddressList
                            //�洢���������û��Ĺ����ַ���˿�
                        if(!addressList.equals("")){
                            String address[] = newAddress.split(":");
                            doSend("7 " + addressList,
                                   address[1] + " " + address[2]);
                            //����ǰѡ�addressList�����������û�����Ϣ���û����ַ�����˿ںŸ��߸�ע����Ǹ��û���
                        }
                        addressList = addressList + newAddress + " ";
                        break;
                    }

                    case 6 : {
                        line = "һ���ͻ��뿪: " + msg + "  NAT��ַ: " + natAddress + " ����˿�: " + natPort;
                        println(line);
                        delAddress(msg, natAddress, natPort);
                        if(!sendAddressList.equals(""))
                            doSend("6 " + msg + ":" + natAddress, sendAddressList);
                        break;
                    }

                    case 7 : break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doSend(String msgSend, String sendAddressList) throws Exception {
        String[] s = sendAddressList.split(" ");
        byte[] data = msgSend.getBytes();
        for(int i =0; i < s.length; i++){
            DatagramPacket pack = new DatagramPacket(data, data.length, InetAddress.getByName(s[i]), Integer.parseInt(s[++i]));
            ds.send(pack);//����İѵ�ַ��Ϣ�����Ѿ�ע��������û���ע��i��ѭ������Ҳ��
        }
    }

    public void delAddress(String msg, String natAddress, int ctrlPort){

        String regEx = msg + ":" + natAddress + ":" + ctrlPort + ":.{1,5}:.{1,5}:.{1,5}:.{1,5} ";
        Pattern p = Pattern.compile(regEx);
        Matcher m=p.matcher(addressList);
        //�����ģʽƥ�䣬��regExƥ�������Ȼ��ȥ����Ҳ����ɾ��
        String s = m.replaceAll("");
        addressList = s;

        regEx = natAddress + " " + ctrlPort + " ";
        p = Pattern.compile(regEx);
        m = p.matcher(sendAddressList);
        s = m.replaceAll("");
        sendAddressList = s;
    }

    public void println(String s) {
        Date nowTime = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa", Locale.US);
        System.out.println(fmt.format(nowTime) + "----" + s);
    }

    public static void main(String[] args) throws Exception {
        new ChatServer(2008).start();
    }
}
