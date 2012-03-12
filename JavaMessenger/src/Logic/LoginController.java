/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.DataAccessObject;
import DAOs.UsersDAO;
import Entities.Users;
import GUI.ApplicationFrame;
import Others.ApplicationState;
import crypto.Hasher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author SysOp
 */
public class LoginController {

    public LoginController() {
    }

    public boolean getAuthenticationData(String login, String pass) {
        System.out.println("Stworzono tryselect");
        if (this.selectAuthentication(login, pass)) {
            //Temps.TrySelect tr = new Temps.TrySelect();
            //List<Entities.Contacts> mylist = tr.getContacts(login);
            //.setJlist(mylist);
            return true;
        } else {
            return false;
        }

    }

    public void setUserIp(String login, ApplicationState applicationState) {
//        System.out.println("WSTAWIAMY: "+login + " APPLICATIONSTATE: " + applicationState.toString());
        Users user;
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(login);
        user = userList.get(0);
        if (applicationState.getLoggedUser() == null) {
               user.setIp("" + this.getMyPublicIP());
                System.out.println("Moje IP publicze " + this.getMyPublicIP());
                System.out.println("JEST");
                applicationState.setLoggedUser(user);
        } else {
            user.setIp(null);
            applicationState.setLoggedUser(null);
            System.out.println("NULL");
        }
        userDao.update(user);
            
//        for (Users u : userList) {
//            if (applicationState.getLoggedUser() == null) {
//                u.setIp("" + this.getMyPublicIP());
//                System.out.println("Moje IP publicze " + this.getMyPublicIP());
//                System.out.println("JEST");
//            } else {
//                u.setIp(null);
//                System.out.println("NULL");
//            }
//            userDao.insert(u);// problem z updatem
//        }


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
    

    public boolean selectAuthentication(String userName, String password) {
        boolean islooged = false;
        String s = Hasher.generateHash(password, Hasher.HASH_SHA512);
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(userName);
        if (userList.isEmpty()) {
            islooged = false;
        } else {
            //List<Users> users = query.getResultList();
            for (Users u : userList) {
                if (u.getPassword().toString().equals("" + s)) {
                    islooged = true;
                }
            }
            System.out.println("Czy jest zalogowany? " + islooged);
        }

        return islooged;
    }
}
