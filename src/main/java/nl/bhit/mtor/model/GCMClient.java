package nl.bhit.mtor.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "GCM_CLIENT")
public class GCMClient extends BaseObject implements Serializable {

    private static final long serialVersionUID = 7382525403256174201L;
    private Long id;
    private User user;
    private String gcmRegistrationId;
    private String device;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    @Column(
            name = "ID",
            unique = true,
            nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GCMClient other = (GCMClient) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        return "GCMClient [id=" + id + ", userId=" + userId + ", gcmRegistrationId=" + gcmRegistrationId + ", device="
                + device + "]";
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
