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

    public Conferences findByMessageId(Long id) {
        Query q = entityManager.createNamedQuery("Conferences.findByMessageId");
        q.setParameter("id", id);
        return (Conferences) wrap(q, "getSingleResult", null, null, false);
    }

    public Conferences findById(Long id) {
        Query q = entityManager.createNamedQuery("Conferences.findById");
        q.setParameter("id", id);
        return (Conferences) wrap(q, "getSingleResult", null, null, false);
    }

    public List<Conferences> findAll() {
        Query q = entityManager.createNamedQuery("Conferences.findAll");
        return (List<Conferences>) wrap(q, "getResultList", null, null, false);
    }
}
