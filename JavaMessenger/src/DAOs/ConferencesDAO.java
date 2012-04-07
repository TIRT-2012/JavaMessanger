/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.Conferences;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class ConferencesDAO extends DataAccessObject{
    
    public ConferencesDAO(DBThreadManager dbTM, Long id){
        super(dbTM, id);
    }

    public Conferences findByMessageId(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Conferences.findByMessageId");
        q.setParameter("id", id);
        return (Conferences) wrap(q, "getSingleResult", null, null, wait);
    }

    public Conferences findById(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Conferences.findById");
        q.setParameter("id", id);
        return (Conferences) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Conferences> findAll(boolean... wait) {
        Query q = entityManager.createNamedQuery("Conferences.findAll");
        return (List<Conferences>) wrap(q, "getResultList", null, null, wait);
    }
}
