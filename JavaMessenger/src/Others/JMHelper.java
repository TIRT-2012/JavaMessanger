/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Klasa udostępnia metodę pomocniczą, pobierającą zewnętrzy adres IP klienta.
 */
public class JMHelper {
    
    /**
     * Metoda zwracająca zewnętrzny adres IP zalogowanego użytkownika
     * @return adres IP użytkownika
     */
    public static String getMyPublicIP() {
        System.out.println("getMyPublicIP()");
        try {
            URL readIp = new URL("http://ip.boo.pl/");
            BufferedReader in = new BufferedReader(new InputStreamReader(readIp.openStream()));
            String lineIn=null;
            String ipAddress=null;
            while((lineIn=in.readLine())!=null){
                if(lineIn.trim().equals("<td>IP: </td>")){
                    ipAddress=in.readLine().trim();
                    ipAddress=ipAddress.replace("<td>", "").replace("</td>", "");
                }
            }
            return ipAddress;

        } catch (Exception e) {
            e.printStackTrace();

            return "Blad odczytu";
        }
    }
}
