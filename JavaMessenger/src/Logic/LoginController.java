/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.ContactsDAO;
import DAOs.UsersDAO;
import Entities.Contacts;
import Entities.Users;
import crypto.Hasher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 *
 * @author SysOp
 */
public class LoginController {

    private Users userObject = null;

    
    private boolean loggedUser = false;

    public LoginController() {
    }

    public Users getUserObject() {
        return userObject;
    }

    public void setUserObject(Users userObject) {
        this.userObject = userObject;
    }
    
    public void setLoggedUser(boolean loggedUser) {
        this.loggedUser = loggedUser;
    }

    public boolean isLoggedUser() {
        return loggedUser;
    }

    public boolean getAuthenticationData(String login, String pass) {
        System.out.println("getAuthenticationData()");
        if (this.selectAuthentication(login, pass))
            return true;
        return false;

    }

    public List<Contacts> getContacts() {
        Long id = null;
        id = userObject.getId();
        ContactsDAO contactDao = new ContactsDAO();
        List<Contacts> contactList = contactDao.findByUserId(1L);
        return contactList;
    }

    public void setUserIp() {
        System.out.println("setUserIp()");
        UsersDAO userDao = new UsersDAO();
        userObject.setIp(this.getMyPublicIP().toString());
        userDao.update(userObject);
        loggedUser = true;
    }

    public void removeUserIp() {
        System.out.println("removeUserIp()");
        UsersDAO userDao = new UsersDAO();
        userObject.setIp(null);
        userDao.update(userObject);
        loggedUser = false;
    }

    public String getMyPublicIP() {
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

    public boolean selectAuthentication(String userName, String password) {
        System.out.println("selectAuthentication()");
        boolean islooged = false;
        String s = Hasher.generateHash(password, Hasher.HASH_SHA512);
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(userName);
        if (userList.isEmpty()) {
            islooged = false;
        } else {
            for (Users u : userList) {
                if (u.getPassword().toString().equals("" + s)) {
                    islooged = true;
                    userObject = u;
                }
            }
            System.out.println("Czy jest zalogowany? " + islooged);
        }

        return islooged;
    }
}
