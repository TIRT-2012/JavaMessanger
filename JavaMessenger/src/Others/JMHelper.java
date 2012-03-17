/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author SysOp
 */
public class JMHelper {
    
    public static String getMyPublicIP() {
        System.out.println("getMyPublicIP()");
        try {
            URL readIp = new URL("http://automation.whatismyip.com/n09230945.asp");
            BufferedReader in = new BufferedReader(new InputStreamReader(readIp.openStream()));
            String ip_address = (in.readLine()).trim();

            return ip_address;

        } catch (Exception e) {
            e.printStackTrace();

            return "Blad odczytu";
        }
    }
}
