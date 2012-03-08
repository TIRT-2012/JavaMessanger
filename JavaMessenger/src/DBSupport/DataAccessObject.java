/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SysOp
 */
public class DataAccessObject {

    protected EntityManager entityManager;
    
    public DataAccessObject(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        entityManager = emf.createEntityManager();
    }

    public void persist(Object entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    public void remove(Object entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

}
