/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Klasa DataAccessObject odpowiada za połączenie aplikacji z bazą danych 
 * i mapowanie danych w postaci relacyjnej do postaci obiektowej.
 */
public class DataAccessObject {

    protected EntityManager entityManager;
    private DBThreadManager dbTM;
    protected long id;
    protected long pos;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public DataAccessObject(DBThreadManager dbTM, Long id, Long pos) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaMessengerPU");
        entityManager = emf.createEntityManager();
        this.dbTM=dbTM;
        this.id=id;
        this.pos=pos;
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
        DBThread t = new DBThread(object, function, parametersClass, parameters);
        long threadId=dbTM.add(t);
        return wait.length>0 && wait[0]? dbTM.getResult(threadId) : null;
    }
    
    public long getId(){
        return id;
    }
    
    public long getPos(){
        return pos;
    }

}