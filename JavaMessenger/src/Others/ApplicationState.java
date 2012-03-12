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

    private Users loggedUser;
    private boolean userState = false;

    public void setUserState(boolean userState) {
        this.userState = userState;
    }

    public boolean isUserState() {
        return userState;
    }
    
    public Users getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }
}
