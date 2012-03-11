/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Logic.LoginController;

/**
 *
 * @author SysOp
 */
public class ApplicationComponents {
    
    public ApplicationComponents(){
        loginController=new LoginController();
        applicationState=new ApplicationState();
    }
    
    private LoginController loginController;
    
    private ApplicationState applicationState;

    public ApplicationState getApplicationState() {
        return applicationState;
    }

    public LoginController getLoginController() {
        return loginController;
    }
    
}
