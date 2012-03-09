/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Piotr
 */
public class JavaMessenger {

    /**
     * @param args the command line arguments
     */
    public JavaMessenger() {
        ApplicationFrame app = new ApplicationFrame();
        app.setLocationRelativeTo(app.getRootPane());
        app.setVisible(true);
    }

    public static void main(String[] args){
        // TODO code application logic here
        JavaMessenger runnerClass = new JavaMessenger();
        //System.out.println(Hasher.generateHash("wtf", Hasher.HASH_SHA512));
    }
}
