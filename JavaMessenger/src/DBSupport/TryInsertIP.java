/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Piotr
 */
public class TryInsertIP {

    boolean setIpFlag = false;

    public TryInsertIP(boolean flag) {
        setIpFlag = flag;
    }

    public void setUserIp(String a) {
        boolean ipUpdate = false;
        boolean error = true;
        while (error) {
            try {


                EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
                EntityManager em = emf.createEntityManager();

                em.getTransaction().begin();

                Query query = em.createNamedQuery("Users.findByUserName");
                query.setParameter("userName", a);
                List<Users> users = query.getResultList();
                for (Users u : users) {
                    System.out.println("setIPFLAG: " + setIpFlag);
                    if (setIpFlag) {
                        u.setIp("" + this.getMyPublicIP());
                        System.out.println("Moje IP publicze " + this.getMyPublicIP());
                    } else {
                        u.setIp(null);
                        System.out.println("Wylogowany");
                    }

                    em.persist(u);
                }
                em.getTransaction().commit();
                em.close();
                emf.close();
                error = false;
            } catch (Exception e) {
                error = true;
            }
        }
    }

    public String getMyPublicIP() {
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
