/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import GUI.ApplicationFrame;

/**
 * Klasa uruchamiająca program i inicjująca niezbędne komponenty.
 */
public class JavaMessenger {
    private final String defaultCryptAlgoritm = "AES";
    private final int defaultCryptSymetricKeySize = 128;
    /**
     * @param args the command line arguments
     */
    ApplicationComponents applicationComponents;
    ApplicationFrame app;
    
    /**
     * Konstruktor klasy ustawia na wartości domyślne konfigurację szyfrowania wiadomości
     * i tworzy główną formatkę programu.
     */
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
