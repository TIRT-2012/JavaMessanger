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
        boolean error = true;
        
        boolean islooged = false;
        while(error){
        try{
            System.out.println(" selectAuthentication ");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();
            System.out.println(" Transakcja rozpoczęta" + " login "+ a + " pass " + b);
            System.out.println(" islogged = "+islooged);
        

            
            Query query = em.createNamedQuery( "Users.findByUserName");
            query.setParameter("userName", a);
            List<Users> users = query.getResultList();
            for (Users u : users) {
                System.out.println (u.getId() + " " + u.getUserName() + " " + u.getPassword());
                System.out.println("Tester ");
                if(u.getPassword().equals(b))
                {islooged = true;
                    System.out.println("Jest zalogowany "+islooged);
                }
            }
            System.out.println("Czy jest zalogowany? "+islooged);
            em.getTransaction().commit();
            em.close();
            emf.close();
            error = false;
            }catch(Exception e){
                error = true;
            }
        }
        
        System.out.println("KONIEC");
        return islooged;
    }
    
}
