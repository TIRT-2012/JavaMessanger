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
 * Klasa odpowiedzialna za akcję logowania użytkownika. W tym celu komunikuję się
 * z bazą danych i autoryzuje użytkownika.
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
    
    /**
     * Funkcja pobiera z bazy danych użytkownika na podstawie jego IP.
     * @param ip IP szukanego użytkownika
     * @return nazwa użytkownika
     */
    public String getUserName(String ip) {
        System.out.println("getUserName()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        Users temp = userDao.findByIp(ip, true);
        applicationComponent.releseDAO(userDao);
        return temp.getUserName();
    }

    /**
     * Funkcja dodaje nowy kontakt dla zalogowanego użytkownika
     * @param name nazwa nowego kontaktu (login użytkownika)
     * @return czy operacja dodania kontaktu się powiodła
     */
    public boolean setContact(String name) {
        System.out.println("setContact()");
        boolean isAdded = false;
        ContactsDAO contactDao = applicationComponent.getContactsDAO();
        Contacts contact = new Contacts();
        contact.setUserId(userObject.getId());
        contact.setName(name);
        contactDao.insert(contact, true);
        applicationComponent.releseDAO(contactDao);
        return isAdded;
    }

    /**
     * Funkcja pobiera listę kontaktów zalogowanego użytkownika
     * @return lista kontaktów
     */
    public List<Contacts> getContacts() {
        Long id = null;
        id = userObject.getId();
        ContactsDAO contactDao = applicationComponent.getContactsDAO();
        List<Contacts> contactList = contactDao.findByUserId(id, true);
        applicationComponent.releseDAO(contactDao);
        return contactList;
    }

    /**
     * Funkcja zwraca listę użytkowników, których login spełnia podane kryterium.
     * @param name kryterium nazwy użytkowników
     * @return lista użytkowników
     */
    public List<Users> getUsers(String name) {
        Long id = null;
        id = userObject.getId();
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> userList = userDao.findByUserName(name, true);
        applicationComponent.releseDAO(userDao);
        return userList;
    }

    /**
     * Funkcja zwraca listę wszystkich użytkowników z bazy danych
     * @return lista użytkowników
     */
    public List<Users> getUsersAll() {
        Long id = null;
        id = userObject.getId();
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> userList = userDao.findAll(true);
        applicationComponent.releseDAO(userDao);
        return userList;
    }

    /**
     * Funkcja pobiera IP użytkownika podanego jako parametr
     * @param name nazwa użytkownika
     * @return IP użytkownika
     */
    public String getAnotherUserIp(String name) {
        System.out.println("getAnotherUserIp()");
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> temp = userDao.findByUserName(name, true);
        applicationComponent.releseDAO(userDao);
        return temp.get(0).getIp();
    }

    /**
     * Metoda ustawia adres IP w bazie na aktualny adres, jaki posiada zalogowany klient.
     */
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

    /**
     * Metoda wylogowywuje aktualnie zalogowanego użytkownika.
     */
    public void logout() {
        removeUserIp();
        applicationComponent.getSSLController().stopServer();
    }

    /**
     * Metoda usuwa IP użytkownika z bazy danych.
     * Jest on przez to oznaczany jako niezalogowany
     */
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

    /**
     * Funkcja sprawdza, czy użytkownik loguje się, używając poprawnych danych.
     * @param userName podana nazwa użytkownika
     * @param password podane hasło użytkownika
     * @return rezultat logowania
     */
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
