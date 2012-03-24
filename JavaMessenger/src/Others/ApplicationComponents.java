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
    
    private LoginController loginController;
    
    public ApplicationComponents() {
        loginController = new LoginController();
        
    }
    
    public LoginController getLoginController() {
        return loginController;
    }
}
