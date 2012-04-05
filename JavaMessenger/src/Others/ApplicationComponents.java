/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Logic.LoginController;
import Logic.SSLControler;

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
