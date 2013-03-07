package nl.bhit.mtor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@Table(
        name = "MESSAGE")
public class MTorMessage extends BaseObject implements Serializable {
    private static final long serialVersionUID = 1775343633035089024L;
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(MTorMessage.class);

    private Long id;
    private String content;
    private Status status;
    private Project project;
    private Date timestamp;
    private boolean resolved;
    private boolean alertSent;

    public MTorMessage() {
        init();
    }

    public void init() {
        log.trace("initing MTorMessage");
        timestamp = new Date();
        resolved = false;
        alertSent = false;
    }

    public MTorMessage(String content) {
        this.content = content;
    }

    @ManyToOne(
            fetch = FetchType.EAGER)
    @JoinColumn(
            name = "PROJECT_FK")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

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

    @Column(
            name = "CONTENT",
            nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(
            name = "STATUS",
            length = 5)
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(
            name = "TIMESTAMP",
            nullable = false)
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Column(
            name = "RESOLVED",
            nullable = false)
    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
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
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MTorMessage other = (MTorMessage) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "MTorMessage [id=" + id + ", content=" + content + ", status=" + status + ", project=" + project
                + ", timestamp=" + timestamp + ", resolved=" + resolved + "]";
    }

    @Column(
            name = "ALERT_SENT",
            nullable = false)
    public boolean isAlertSent() {
        return alertSent;
    }

    public void setAlertSent(boolean alertSent) {
        this.alertSent = alertSent;
    }

}
