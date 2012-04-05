/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.Contacts;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class ContactsDAO extends DataAccessObject{
    
    public ContactsDAO(DBThreadManager dbTM){
        super(dbTM);
    }
    
    public List<Contacts> findByName(String name, boolean... wait) {
        Query q = entityManager.createNamedQuery("Contacts.findByName");
        q.setParameter("name", name);
        return (List<Contacts>) wrap(q, "getResultList", null, null, wait);
    }

    public List<Contacts> findByUserId(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Contacts.findByUserId");
        q.setParameter("userId", id);
        return (List<Contacts>) wrap(q, "getResultList", null, null, wait);
    }

    public List<Contacts> findByNumber(int number, boolean... wait) {
        Query q = entityManager.createNamedQuery("Contacts.findByNumber");
        q.setParameter("number", number);
        return (List<Contacts>) wrap(q, "getResultList", null, null, wait);
    }

    public Contacts findById(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Contacts.findById");
        q.setParameter("id", id);
        return (Contacts) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Contacts> findAll(boolean... wait) {
        Query q = entityManager.createNamedQuery("Contacts.findAll");
        return (List<Contacts>) wrap(q, "getResultList", null, null, wait);
    }
}
