/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import Entities.Users;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class UsersDAO extends DataAccessObject {
    
    public UsersDAO(DBThreadManager dbTM, Long id, Long pos){
        super(dbTM, id, pos);
    }

    public List<Users> findByUserName(String userName, boolean... wait) {
        Query q = entityManager.createNamedQuery("Users.findByUserName");
        q.setParameter("userName", userName);
        return (List<Users>) wrap(q, "getResultList", null, null, wait);
    }

    public Users findByIp(String ip, boolean... wait) {
        Query q = entityManager.createNamedQuery("Users.findByIp");
        q.setParameter("ip", ip);
        return (Users) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Users> findByPassword(String password, boolean... wait) {
        Query q = entityManager.createNamedQuery("Users.findByPassword");
        q.setParameter("password", password);
        return (List<Users>) wrap(q, "getResultList", null, null, wait);
    }

    public Users findById(Long id, boolean... wait) {
        Query q = entityManager.createNamedQuery("Users.findById");
        q.setParameter("id", id);
        return (Users) wrap(q, "getSingleResult", null, null, wait);
    }

    public List<Users> findAll(boolean... wait) {
        Query q = entityManager.createNamedQuery("Users.findAll");
        return (List<Users>) wrap(q, "getResultList", null, null, wait);
    }
}
