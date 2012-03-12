/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

import DAOs.UsersDAO;
import Entities.Users;
import crypto.Hasher;
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
            user.setPassword(Hasher.generateHash(password, Hasher.HASH_SHA512));
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
//        Users tmp;//=uDAO.findById(2L);
////        out.println(tmp.getUserName());
        
            Users test;
            List<Users> mylist = uDAO.findByUserName("wind12");
            test = mylist.get(0);
            System.out.println( " XXXXXXX " + test.getPassword().toString());
            test.setUserName("Marca");
//            test.setUserName("TestDAOs2");
//            test.setIp("123.123.123.2");
//            test.setPassword("KUTAS");
//            uDAO.insert(test);
            uDAO.update(test);
//        List<Users> list=uDAO.findByUserName("TestDAOs1");
//        for(Iterator it=list.iterator(); it.hasNext();){
//            Users tmp=(Users) it.next();
//            out.println(tmp.getUserName());
//        }
    }
}
