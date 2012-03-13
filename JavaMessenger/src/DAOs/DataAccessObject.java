/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 *
 * @author SysOp
 */
public class DataAccessObject {

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    final static int MAX_TRY = 5;

    public DataAccessObject() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        entityManager = emf.createEntityManager();
    }

    public boolean insert(Object entity) {
        return (Boolean) wrap(entityManager, "persist", new Class[]{Object.class}, new Object[]{entity}, true);
    }

    public boolean remove(Object entity) {
        return (Boolean) wrap(entityManager, "remove", new Class[]{Object.class}, new Object[]{entity}, true);
    }
    
    public boolean update(Object entity) {
        return (Boolean) wrap(entityManager, "merge", new Class[]{Object.class}, new Object[]{entity}, true);
    }

    public Object wrap(Object object, String function, Class[] parametersClass, Object[] parameters, boolean isTransaction) {
        Object result = null;
        boolean repeat = false;
        int errorCount = 0;
        do {
            try {
                if (isTransaction) {
                    entityManager.getTransaction().begin();
                }
                Method method = object.getClass().getMethod(function, parametersClass);
                result = method.invoke(object, parameters);
                if (isTransaction) {
                    entityManager.getTransaction().commit();
                }
                repeat = false;
            } catch (RollbackException rbe){
                errorCount=MAX_TRY;
            } catch (Exception e) {
                if (isTransaction && entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                repeat = true;
                errorCount++;
            }
        } while (repeat && errorCount < MAX_TRY);
        if(errorCount >= MAX_TRY )
            return Boolean.FALSE;
        else
            return result!=null? result:Boolean.TRUE;
    }
}
