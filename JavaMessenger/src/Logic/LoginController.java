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
        applicationComponent = ac;
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

    /*
     * public boolean getAuthenticationData(String login, String pass) {
     * System.out.println("getAuthenticationData()"); if
     * (this.selectAuthentication(login, pass)) return true; return false;
     *
     * }
     */
    public String getUserName(String ip) {
        System.out.println("getUserName()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        Users temp = userDao.findByIp(ip, true);
        applicationComponent.releseDAO(userDao);
        return temp.getUserName();
    }

    public List<Contacts> getContacts() {
        Long id = null;
        id = userObject.getId();
        ContactsDAO contactDao = applicationComponent.getContactsDAO();
        List<Contacts> contactList = contactDao.findByUserId(id, true);
        applicationComponent.releseDAO(contactDao);
        return contactList;
    }

    public String getAnotherUserIp(String name) {
        System.out.println("getAnotherUserIp()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> temp = userDao.findByUserName(name, true);
        applicationComponent.releseDAO(userDao);
        return temp.get(0).getIp();
    }

    public void setUserIp() {
        System.out.println("setUserIp()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        String name = userObject.getUserName();
        if (!((name.equals("hpiotrek89")) || (name.equals("htesto")) || (name.equals("hwind44")) || (name.equals("hpiotrek")))) {
            userObject.setIp(JMHelper.getMyPublicIP());
        }
        userDao.update(userObject);
        applicationComponent.releseDAO(userDao);
        loggedUser = true;
    }
    
    public void logout(){
        removeUserIp();
        applicationComponent.getSSLController().stopServer();
    }

    private void removeUserIp() {
        System.out.println("removeUserIp()");
        String name = userObject.getUserName();
        if (!((name.equals("hpiotrek89")) || (name.equals("htesto")) || (name.equals("hwind44")) || (name.equals("hpiotrek")))) {
            UsersDAO userDao = applicationComponent.getUsersDAO();
            userObject.setIp(null);
            userDao.update(userObject);
            applicationComponent.releseDAO(userDao);
            loggedUser = false;
        }
    }

    public boolean selectAuthentication(String userName, String password) {
        System.out.println("selectAuthentication()");
        boolean islooged = false;
        String s = Hasher.generateHash(password, Hasher.HASH_SHA512);
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> userList = userDao.findByUserName(userName, true);
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
        applicationComponent.releseDAO(userDao);
        return islooged;
    }
}
