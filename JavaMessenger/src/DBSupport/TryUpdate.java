/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class TryUpdate {
    
    public static void main(String[] a){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        Query query = em.createNamedQuery( "Users.findById");
        query.setParameter("id", 8);
        List<Users> users = query.getResultList();
        for (Users u : users) {
            System.out.println (u.getId() + " " + u.getUserName());
            u.setUserName("testUpdate");
            em.persist(u);
        }
//        Users user = em.find(Users.class, 1L); //find by id
//        user.setUserName("TestUpdate");
//        em.persist(user);
        
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
