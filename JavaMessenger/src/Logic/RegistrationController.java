/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.UsersDAO;
import Entities.Users;
import Others.ApplicationComponents;
import crypto.Hasher;
import java.util.List;

/**
 *
 * Klasa odpowiedzialna za stworzenie nowego konta użytkownika
 */
public class RegistrationController {
    
    private ApplicationComponents applicationComponent;
    
    public RegistrationController(ApplicationComponents ac){
        applicationComponent=ac;
    }
    
    /**
     * Funkcja odpowiada za stworzenie nowego konta użytkownika w bazie danych.
     * @param userName
     * @param password
     * @return czy operacja utworzenia konta użytkownika się powiodła
     */
    public boolean createAccount(String userName, String password){
        System.out.println("createAccount()");
        
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> userList = userDao.findByUserName(userName, true);
        boolean isEmpty = userList.isEmpty();
        
        if(isEmpty){
            Users user = new Users();
            user.setUserName(userName);
            user.setPassword(Hasher.generateHash(password, Hasher.HASH_SHA512));
            userDao.insert(user);
        }
        applicationComponent.releseDAO(userDao);
        return isEmpty;
    }
    
}
