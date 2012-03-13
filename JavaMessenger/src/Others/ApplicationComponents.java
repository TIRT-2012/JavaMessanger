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
    }
    
    private LoginController loginController;
    
    public LoginController getLoginController() {
        return loginController;
    }
    
}
