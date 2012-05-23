/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.Messages;
import java.util.List;
import javax.persistence.Query;

/**
 * Implementacja obiektu DAO dla tabeli messenges.
 */
public class MessagesDAO extends DataAccessObject{
    
    public MessagesDAO(DBThreadManager dbTM, Long id, Long pos){
        super(dbTM, id, pos);
    }
    
    public List<Messages> findBySenderId(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Messages.findBySenderId");
        q.setParameter("id", id);
        return (List<Messages>) wrap(q, "getResultList", null, null, wait);
    }

    public Messages findByTimestamp(String timestamp, boolean... wait) {
        Query q = entityManager.createNamedQuery("Messages.findByTimestamp");
        q.setParameter("timestamp", timestamp);
        return (Messages) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Messages> findByContent(String content, boolean... wait) {
        Query q = entityManager.createNamedQuery("Messages.findByContent");
        q.setParameter("content", content);
        return (List<Messages>) wrap(q, "getResultList", null, null, wait);
    }

    public Messages findById(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Messages.findById");
        q.setParameter("id", id);
        return (Messages) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Messages> findAll(boolean... wait) {
        Query q = entityManager.createNamedQuery("Messages.findAll");
        return (List<Messages>) wrap(q, "getResultList", null, null, wait);
    }
}
