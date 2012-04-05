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
 * @author PWr
 */
public class RegistrationController {
    
    private ApplicationComponents applicationComponent;
    
    public RegistrationController(ApplicationComponents ac){
        applicationComponent=ac;
    }
    
    public boolean createAccount(String userName, String password){
        System.out.println("createAccount()");
        
        UsersDAO userDao = applicationComponent.getUsersDAO();
        List<Users> userList = userDao.findByUserName(userName);
        boolean isEmpty = userList.isEmpty();
        
        if(isEmpty){
            Users user = new Users();
            user.setUserName(userName);
            user.setPassword(Hasher.generateHash(password, Hasher.HASH_SHA512));
            userDao.insert(user);
        }
        
        return isEmpty;
    }
    
}
