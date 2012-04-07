/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SysOp
 */
@Entity
@Table(name = "jmcdata.user_to_conference")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserToConference.findAll", query = "SELECT u FROM UserToConference u"),
    @NamedQuery(name = "UserToConference.findById", query = "SELECT u FROM UserToConference u WHERE u.id = :id"),
    @NamedQuery(name = "UserToConference.findByUserId", query = "SELECT u FROM UserToConference u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserToConference.findByConferenceId", query = "SELECT u FROM UserToConference u WHERE u.conferenceId = :conferenceId")})
public class UserToConference implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "conference_id")
    private int conferenceId;

    public UserToConference() {
    }

    public UserToConference(Long id) {
        this.id = id;
    }

    public UserToConference(Long id, int userId, int conferenceId) {
        this.id = id;
        this.userId = userId;
        this.conferenceId = conferenceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserToConference)) {
            return false;
        }
        UserToConference other = (UserToConference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBSupport.UserToConference[ id=" + id + " ]";
    }
    
}
