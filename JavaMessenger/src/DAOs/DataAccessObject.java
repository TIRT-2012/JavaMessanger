/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author SysOp
 */
public class DataAccessObject {

    protected EntityManager entityManager;
    private DBThreadManager dbTM;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public DataAccessObject(DBThreadManager dbTM) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        entityManager = emf.createEntityManager();
        this.dbTM=dbTM;
    }

    public void insert(Object entity, boolean... wait) {
        wrap(entityManager, "persist", new Class[]{Object.class}, new Object[]{entity}, wait);
    }

    public void remove(Object entity, boolean... wait) {
        wrap(entityManager, "remove", new Class[]{Object.class}, new Object[]{entity}, wait);
    }

    public void update(Object entity, boolean... wait) {
        wrap(entityManager, "merge", new Class[]{Object.class}, new Object[]{entity}, wait);
    }

    protected Object wrap(Object object, String function, Class[] parametersClass, Object[] parameters, boolean... wait) {
        Object result = "";
        DBThread t = new DBThread(object, function, parametersClass, parameters);
        long id=dbTM.add(t);
        return wait.length>0 && wait[0]? dbTM.getResult(id) : null;
    }

}