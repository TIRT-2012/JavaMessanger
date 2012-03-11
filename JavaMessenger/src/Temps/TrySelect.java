/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Temps;

import Entities.Users;
import crypto.Hasher;
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

    public boolean selectAuthentication(String userName, String password) {
        boolean error = true;
        String s = Hasher.generateHash(password, Hasher.HASH_SHA512);
        boolean islooged = false;
        while (error) {
            try {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
                EntityManager em = emf.createEntityManager();

                em.getTransaction().begin();
                Query query = em.createNamedQuery("Users.findByUserName");
                query.setParameter("userName", userName);
                if (query.getResultList().isEmpty()) {
                    islooged = false;
                } else {
                    List<Users> users = query.getResultList();
                    for (Users u : users) {
                        if (u.getPassword().toString().equals("" + s)) {
                            islooged = true;
                        }
                    }
                    System.out.println("Czy jest zalogowany? " + islooged);
                }
                em.getTransaction().commit();
                em.close();
                emf.close();
                error = false;
            } catch (Exception e) {
                error = true;
            }
        }
        return islooged;
    }
}
