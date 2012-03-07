/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBSupport;

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
@Table(name = "jmcdata.conferences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conferences.findAll", query = "SELECT c FROM Conferences c"),
    @NamedQuery(name = "Conferences.findById", query = "SELECT c FROM Conferences c WHERE c.id = :id"),
    @NamedQuery(name = "Conferences.findByMessageId", query = "SELECT c FROM Conferences c WHERE c.messageId = :messageId")})
public class Conferences implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "message_id")
    private int messageId;

    public Conferences() {
    }

    public Conferences(Long id) {
        this.id = id;
    }

    public Conferences(Long id, int messageId) {
        this.id = id;
        this.messageId = messageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
        if (!(object instanceof Conferences)) {
            return false;
        }
        Conferences other = (Conferences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBSupport.Conferences[ id=" + id + " ]";
    }
    
}
