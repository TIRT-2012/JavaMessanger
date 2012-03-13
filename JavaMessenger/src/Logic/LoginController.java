/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.ContactsDAO;
import DAOs.DataAccessObject;
import DAOs.UsersDAO;
import Entities.Contacts;
import Entities.Users;
import Others.ApplicationState;
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

    private boolean loggedUser = false;

    public LoginController() {
    }

    public void setLoggedUser(boolean loggedUser) {
        this.loggedUser = loggedUser;
    }

    public boolean isLoggedUser() {
        return loggedUser;
    }

    public boolean getAuthenticationData(String login, String pass) {
        System.out.println("getAuthenticationData()");
        if (this.selectAuthentication(login, pass)) {
            return true;
        } else {
            return false;
        }

    }

    public List<Contacts> getContacts(String userName) {
        Long id = null;
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(userName);
        for (Users u : userList) {
            id = (Long) u.getId();
        }
        ContactsDAO contactDao = new ContactsDAO();
        List<Contacts> contactList = contactDao.findByUserId(id);
        return contactList;
    }

    public void setUserIp(String login, ApplicationState applicationState) {
        System.out.println("setUserIp()");
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(login);
        for (Users u : userList) {
            u.setIp(this.getMyPublicIP().toString());
            applicationState.setLoggedUser(u);
            applicationState.setUserState(true);
            loggedUser = true;
            userDao.update(u);
        }
    }

    public void removeUserIp(String login, ApplicationState applicationState) {
        System.out.println("removeUserIp()");
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(login);
        for (Users u : userList) {
            u.setIp(null);
            applicationState.setLoggedUser(null);
            applicationState.setUserState(false);
            loggedUser = false;
            userDao.update(u);
        }
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
                }
            }
            System.out.println("Czy jest zalogowany? " + islooged);
        }

        return islooged;
    }
}
