/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.ApplicationFrame;

/**
 *
 * @author Piotr
 */
public class JavaMessenger {
    private final String defaultCryptAlgoritm = "AES";
    private final int defaultCryptSymetricKeySize = 128;
    /**
     * @param args the command line arguments
     */
    ApplicationComponents applicationComponents;
    ApplicationFrame app;
    public JavaMessenger() {
        applicationComponents=ApplicationComponents.getInstance();
        
        applicationComponents.getSSLController().setAlgorithm(defaultCryptAlgoritm);
        applicationComponents.getSSLController().setKeySize(defaultCryptSymetricKeySize);
        
        app = new ApplicationFrame();
        app.setApplicationComponents(applicationComponents);
        app.setLocationRelativeTo(app.getRootPane());
        app.setVisible(true);
    }
    
    public JavaMessenger(ApplicationComponents applicationComponents, ApplicationFrame app)
    {
        this.applicationComponents = applicationComponents;
        this.app = app;
    }        
    
    public static void main(String[] args) {
        // TODO code application logic here
        JavaMessenger runnerClass = new JavaMessenger();
    }
}
