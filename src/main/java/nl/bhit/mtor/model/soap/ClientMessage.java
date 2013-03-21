package nl.bhit.mtor.model.soap;

import java.io.Serializable;

import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Status;

import org.springframework.beans.BeanUtils;

/**
 * this SoapMessage exposes the method of the Message which are needed in the SOAP interface.
 * 
 * @author tibi
 */
public class ClientMessage implements Serializable {
    private static final long serialVersionUID = -26773897872583055L;
    private String content;
    private Status status;
    private Long projectId;

    public ClientMessage(MTorMessage source) {
        BeanUtils.copyProperties(source, this);
        this.setProjectId(source.getProject().getId());
    }

    public ClientMessage() {
        // default cunstructor
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
