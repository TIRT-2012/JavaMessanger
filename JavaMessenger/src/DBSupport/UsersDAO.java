/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author SysOp
 */
public class UsersDAO extends DataAccessObject {

    public List<Users> findByUserName(String userName) {
        Query q = entityManager.createNamedQuery("Users.findByUserName");
        q.setParameter("userName", userName);
        return (List<Users>) q.getResultList();
    }

    public Users findByIp(String ip) {
        Query q = entityManager.createNamedQuery("Users.findByIp");
        q.setParameter("ip", ip);
        return (Users) q.getSingleResult();
    }

    public List<Users> findByPassword(String password) {
        Query q = entityManager.createNamedQuery("Users.findByPassword");
        q.setParameter("password", password);
        return (List<Users>) q.getResultList();
    }
    
    public Users findById(Long id){
        Query q = entityManager.createNamedQuery("Users.findById");
        q.setParameter("id", id);
        return (Users) q.getSingleResult();
    }
    
    public List<Users> findAll(){
        Query q = entityManager.createNamedQuery("Users.findAll");
        return (List<Users>) q.getResultList();
    }
}
