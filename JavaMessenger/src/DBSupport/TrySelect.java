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
 * @author Piotr
 */
public class TrySelect {

    public TrySelect() {
    }
    
    public boolean selectAuthentication(String a, String b)
    {
        boolean islooged = false;
        System.out.println(" selectAuthentication : "+ a + " , " + b);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        System.out.println(" TRANSACTRION BEGIN");
        
        Query query = em.createNamedQuery( "Users.findByUserName");
        //query.setParameter("id", 1L);
        query.setParameter(a, 1L);
        List<Users> users = query.getResultList();
        for (Users u : users) {
            System.out.println (u.getId() + " " + u.getUserName() + " " + u.getPassword());
            if(u.getPassword().equals(b))
            {islooged = true;
            }
              
         }
        
        em.getTransaction().commit();
        em.close();
        emf.close();
        return islooged;
    }
    
}
