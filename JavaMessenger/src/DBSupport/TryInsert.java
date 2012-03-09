/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import static java.lang.System.out;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
/**
 *
 * @author SysOp
 */
public class TryInsert {
    public static final int STATUS_OK = 0;
    public static final int STATUS_INVALID_DATA = 1;
    public static final int STATUS_CONNECTION_ERROR = 2;
            
    public int createAccount(String userName, String password){
        try{
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            Users user=new Users();
            user.setUserName(userName);
            //user.setIp("177.123.123.126");
            user.setPassword(password);
            em.persist(user);

            em.getTransaction().commit();
            em.close();
            emf.close();
        }catch(RollbackException ex){
            return STATUS_INVALID_DATA;
        }catch(Exception ex){
            return STATUS_CONNECTION_ERROR;
        }
        
        return STATUS_OK;
    }
    
    public static void main(String[] a){
        UsersDAO uDAO= new UsersDAO();
        Users tmp;//=uDAO.findById(2L);
//        out.println(tmp.getUserName());
        List<Users> list=uDAO.findAll();
        for(Iterator it=list.iterator(); it.hasNext();){
            tmp=(Users) it.next();
            out.println(tmp.getUserName());
        }
    }
}
