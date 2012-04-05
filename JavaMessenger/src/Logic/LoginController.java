/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.ContactsDAO;
import DAOs.UsersDAO;
import Entities.Contacts;
import Entities.Users;
import Others.ApplicationComponents;
import Others.JMHelper;
import crypto.Hasher;
import java.util.List;

/**
 *
 * @author SysOp
 */
public class LoginController {

    private Users userObject = null;
    private ApplicationComponents applicationComponent;

    
    private boolean loggedUser = false;

    public LoginController(ApplicationComponents ac) {
        applicationComponent=ac;
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

    /*public boolean getAuthenticationData(String login, String pass) {
        System.out.println("getAuthenticationData()");
        if (this.selectAuthentication(login, pass))
            return true;
        return false;

    }*/

    public String getUserName(String ip)
    {
        System.out.println("getUserName()");
        UsersDAO userDao = new UsersDAO();
        Users temp = userDao.findByIp(ip);
        return temp.getUserName();
    }
    
    public List<Contacts> getContacts() {
        Long id = null;
        id = userObject.getId();
        ContactsDAO contactDao = applicationComponent.getContactsDAO();
        List<Contacts> contactList = contactDao.findByUserId(1L);
        return contactList;
    }

    public String getAnotherUserIp(String name)
    {
        System.out.println("getAnotherUserIp()");
        UsersDAO userDao = new UsersDAO();
        List<Users> temp = userDao.findByUserName(name);
        return temp.get(0).getIp();
    }
    
    public void setUserIp() {
        System.out.println("setUserIp()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        userObject.setIp(JMHelper.getMyPublicIP());
        userDao.update(userObject);
        loggedUser = true;
    }

    public void removeUserIp() {
        System.out.println("removeUserIp()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        userObject.setIp(null);
        userDao.update(userObject);
        loggedUser = false;
    }

    public boolean selectAuthentication(String userName, String password) {
        System.out.println("selectAuthentication()");
        boolean islooged = false;
        String s = Hasher.generateHash(password, Hasher.HASH_SHA512);
        UsersDAO userDao = applicationComponent.getUsersDAO();
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
