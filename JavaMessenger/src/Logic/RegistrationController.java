/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.UsersDAO;
import Entities.Users;
import GUI.AuthenticationFrame;
import crypto.Hasher;
import javax.persistence.RollbackException;

/**
 *
 * @author PWr
 */
public class RegistrationController {
    public static final int STATUS_OK = 0;
    public static final int STATUS_INVALID_DATA = 1;
    public static final int STATUS_CONNECTION_ERROR = 2;
    public String userName;
    public String password;
    
    public int createAccount(String userName, String password){
        System.out.println("createAccount()");
        
        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(Hasher.generateHash(password, Hasher.HASH_SHA512));
        
        UsersDAO userDao = new UsersDAO();
        userDao.insert(user);
            
        this.userName = userName;
        this.password = password;
        
        return STATUS_OK;
    }
    
}
