/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import DAOs.UsersDAO;
import Entities.Users;
import crypto.Hasher;
import java.util.List;

/**
 *
 * @author PWr
 */
public class RegistrationController {
    
    public boolean createAccount(String userName, String password){
        System.out.println("createAccount()");
        
        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(Hasher.generateHash(password, Hasher.HASH_SHA512));
        
        UsersDAO userDao = new UsersDAO();
        List<Users> userList = userDao.findByUserName(userName);
        
        boolean isEmpty = userList.isEmpty();
        
        if(isEmpty)
            userDao.insert(user);
        
        return isEmpty;
    }
    
}
