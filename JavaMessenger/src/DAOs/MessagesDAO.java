/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.Messages;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class MessagesDAO extends DataAccessObject{
    
    public List<Messages> findBySenderId(Long id) {
        Query q = entityManager.createNamedQuery("Messages.findBySenderId");
        q.setParameter("id", id);
        return (List<Messages>) wrap(q, "getResultList", null, null, false);
    }

    public Messages findByIp(String timestamp) {
        Query q = entityManager.createNamedQuery("Messages.findByTimestamp");
        q.setParameter("timestamp", timestamp);
        return (Messages) wrap(q, "getSingleResult", null, null, false);
    }

    public List<Messages> findByContent(String content) {
        Query q = entityManager.createNamedQuery("Messages.findByContent");
        q.setParameter("content", content);
        return (List<Messages>) wrap(q, "getResultList", null, null, false);
    }

    public Messages findById(Long id) {
        Query q = entityManager.createNamedQuery("Messages.findById");
        q.setParameter("id", id);
        return (Messages) wrap(q, "getSingleResult", null, null, false);
    }

    public List<Messages> findAll() {
        Query q = entityManager.createNamedQuery("Messages.findAll");
        return (List<Messages>) wrap(q, "getResultList", null, null, false);
    }
}
