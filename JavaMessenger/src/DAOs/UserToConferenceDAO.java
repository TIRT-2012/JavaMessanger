/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.UserToConference;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class UserToConferenceDAO extends DataAccessObject{
    
    public List<UserToConference> findByUserId(Long id) {
        Query q = entityManager.createNamedQuery("UserToConference.findByUserId");
        q.setParameter("id", id);
        return (List<UserToConference>) wrap(q, "getResultList", null, null, false);
    }

    public List<UserToConference> findByConferenceId(Long id) {
        Query q = entityManager.createNamedQuery("UserToConference.findByConferenceId");
        q.setParameter("id", id);
        return (List<UserToConference>) wrap(q, "getResultList", null, null, false);
    }

    public UserToConference findById(Long id) {
        Query q = entityManager.createNamedQuery("UserToConference.findById");
        q.setParameter("id", id);
        return (UserToConference) wrap(q, "getSingleResult", null, null, false);
    }

    public List<UserToConference> findAll() {
        Query q = entityManager.createNamedQuery("UserToConference.findAll");
        return (List<UserToConference>) wrap(q, "getResultList", null, null, false);
    }
}
