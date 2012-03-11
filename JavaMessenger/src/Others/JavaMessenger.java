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

    /**
     * @param args the command line arguments
     */
    public JavaMessenger() {
        ApplicationComponents applicationComponents=new ApplicationComponents();
        ApplicationFrame app = new ApplicationFrame();
//        app.setApplicationComponents(applicationComponents);
        app.setLocationRelativeTo(app.getRootPane());
        app.setVisible(true);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        JavaMessenger runnerClass = new JavaMessenger();
    }
}
