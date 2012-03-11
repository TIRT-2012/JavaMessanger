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

    public List<Contacts> getContacts(String userName) {
        Long id = null;
        boolean error1 = true;
        boolean error2 = true;
        List<Contacts> helperList = null;
        while (error1) {
            try {
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                Query query = em.createNamedQuery("Users.findByUserName");
                query.setParameter("userName", userName);
                List<Users> users = query.getResultList();
                for (Users u : users) {
                    id = (Long) u.getId();
                    System.out.println("Znalezione id = " + id);
                }
                em.getTransaction().commit();
                em.close();
                emf.close();
                error1 = false;

            } catch (Exception e) {
                error1 = true;
            }
        }
        System.out.println("Przechodze dalej");
//        while (error2) {
//            try {
//                EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
//                System.out.println("Ok1 "+error2);
//                EntityManager em = emf.createEntityManager();
//                System.out.println("Ok2 "+error2);
//                em.getTransaction().begin();
//                System.out.println("Zaczelo sie "+error2);
//                Query query = em.createNamedQuery("Contacts.findByUserId");
//                query.setParameter("userId", id);
//                System.out.println(""+query.toString());
//                helperList = query.getResultList();
//                 
//            } catch (Exception e) {
//                error2 = true;
//            }
//        }
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
                System.out.println("Ok1 "+error2);
                EntityManager em = emf.createEntityManager();
                System.out.println("Ok2 "+error2);
                em.getTransaction().begin();
                System.out.println("Zaczelo sie "+error2);
                Query query = em.createNamedQuery("Contacts.findByUserId");
                query.setParameter("userId", id);
                System.out.println(""+query.toString());
                helperList = query.getResultList();
        return helperList;

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
