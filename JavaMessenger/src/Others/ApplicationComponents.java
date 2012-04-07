/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import DAOs.ConferencesDAO;
import DAOs.ContactsDAO;
import DAOs.DBThreadManager;
import DAOs.DataAccessObject;
import DAOs.MessagesDAO;
import DAOs.UserToConferenceDAO;
import DAOs.UsersDAO;
import Logic.LoginController;
import Logic.RegistrationController;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import Logic.SSLController;

/**
 *
 * @author SysOp
 */
public class ApplicationComponents {

    private LoginController loginController;
    private RegistrationController registrationController;
    private SSLController sslController;
    private DataAccessObject[][] dataAccessObjects;
    private boolean[][] usageArray;
    private static final int maxDAOs = 3;
    private static final String[] namesSequence = {"ConferencesDAO", "ContactsDAO", "MessagesDAO", "UserToConferenceDAO", "UsersDAO"};
    private HashMap identifiers;
    private DBThreadManager dbtm;

    public ApplicationComponents() {
        loginController = new LoginController(this);
        registrationController=new RegistrationController(this);
        sslController=new SSLController(this);
        dbtm=new DBThreadManager();
        identifiers = new HashMap();
        for (int i = 0; i < namesSequence.length; i++) {
            identifiers.put(namesSequence[i], i);
        }
        dataAccessObjects=new DataAccessObject[namesSequence.length][];
        for (int i = 0; i < dataAccessObjects.length; i++) {
            dataAccessObjects[i] = new DataAccessObject[maxDAOs];
        }
        usageArray = new boolean[5][maxDAOs];
        for (int i = 0; i < usageArray.length; i++) {
            for (int j = 0; j < maxDAOs; j++) {
                usageArray[i][j] = false;
            }
        }
    }

    public LoginController getLoginController() {
        return loginController;
    }
    
    public RegistrationController getRegistrationController(){
        return registrationController;
    }
    
    public SSLController getSSLController(){
        return sslController;
    }

    public ConferencesDAO getConferencesDAO() {
        return (ConferencesDAO) getDAO("ConferencesDAO");
    }

    public ContactsDAO getContactsDAO() {
        return (ContactsDAO) getDAO("ContactsDAO");
    }

    public MessagesDAO getMessagesDAO() {
        return (MessagesDAO) getDAO("MessagesDAO");
    }

    public UserToConferenceDAO getUserToConferenceDAO() {
        return (UserToConferenceDAO) getDAO("UserToConferenceDAO");
    }

    public UsersDAO getUsersDAO() {
        return (UsersDAO) getDAO("UsersDAO");
    }

    private synchronized DataAccessObject getDAO(String name) {
        DataAccessObject result = null;
        int id=((Integer)identifiers.get(name)).intValue();
        for (int i = 0; i < usageArray[id].length; i++) {
            if (!usageArray[id][i]) {
                if (dataAccessObjects[id][i] == null) {
                    initializeDAO(name, id, i);
                }
                result = dataAccessObjects[id][i];
                usageArray[id][i]=true;
                break;
            }
        }
        return result;
    }
    
    private void initializeDAO(String name, int id, int i){
        try{
            Class[] params=new Class[] {DBThreadManager.class, Long.class, Long.class};
            Constructor classConstructor=Class.forName("DAOs."+name).getConstructor(params);
            dataAccessObjects[id][i]= (DataAccessObject) classConstructor.newInstance(dbtm, (long)id, (long)i);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized void releseDAO(DataAccessObject dao){
        usageArray[(int)dao.getId()][(int)dao.getPos()]=false;
    }
    
    
}
