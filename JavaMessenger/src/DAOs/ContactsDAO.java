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
    
    public List<Contacts> findByName(String name) {
        Query q = entityManager.createNamedQuery("Contacts.findByName");
        q.setParameter("name", name);
        return (List<Contacts>) wrap(q, "getResultList", null, null, false);
    }

    public List<Contacts> findByUserId(Long id) {
        Query q = entityManager.createNamedQuery("Contacts.findByUserId");
        q.setParameter("id", id);
        return (List<Contacts>) wrap(q, "getResultList", null, null, false);
    }

    public List<Contacts> findByNumber(int number) {
        Query q = entityManager.createNamedQuery("Contacts.findByNumber");
        q.setParameter("number", number);
        return (List<Contacts>) wrap(q, "getResultList", null, null, false);
    }

    public Contacts findById(Long id) {
        Query q = entityManager.createNamedQuery("Contacts.findById");
        q.setParameter("id", id);
        return (Contacts) wrap(q, "getSingleResult", null, null, false);
    }

    public List<Contacts> findAll() {
        Query q = entityManager.createNamedQuery("Contacts.findAll");
        return (List<Contacts>) wrap(q, "getResultList", null, null, false);
    }
}
