/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author SysOp
 */
public class TryInsert {
    
    
    
    public static void main(String[] a){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        Users user=new Users();
        user.setUserName("Kapitan Bomba");
        user.setIp("123.120.113.120");
        user.setPassword("secret1");
        em.persist(user);
        
        
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}