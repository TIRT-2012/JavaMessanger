/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Entities.Users;

/**
 *
 * @author SysOp
 */
public class ApplicationState {
    private Users loggedUser = null;
    
    public Users getLoggedUser(){
        return loggedUser;
    }
    
    public void setLoggedUser(Users loggedUser){
        this.loggedUser=loggedUser;
    }
}
