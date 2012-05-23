/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.UserToConference;
import java.util.List;
import javax.persistence.Query;

/**
 * Implementacja obiektu DAO dla tabeli user_to_conference
 */
public class UserToConferenceDAO extends DataAccessObject{
    
    public UserToConferenceDAO(DBThreadManager dbTM, Long id, Long pos){
        super(dbTM, id, pos);
    }
    
    public List<UserToConference> findByUserId(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("UserToConference.findByUserId");
        q.setParameter("id", id);
        return (List<UserToConference>) wrap(q, "getResultList", null, null, wait);
    }

    public List<UserToConference> findByConferenceId(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("UserToConference.findByConferenceId");
        q.setParameter("id", id);
        return (List<UserToConference>) wrap(q, "getResultList", null, null, wait);
    }

    public UserToConference findById(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("UserToConference.findById");
        q.setParameter("id", id);
        return (UserToConference) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<UserToConference> findAll(boolean... wait) {
        Query q = entityManager.createNamedQuery("UserToConference.findAll");
        return (List<UserToConference>) wrap(q, "getResultList", null, null, wait);
    }
}
