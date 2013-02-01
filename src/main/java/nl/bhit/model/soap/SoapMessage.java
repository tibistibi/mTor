package nl.bhit.model.soap;

import nl.bhit.model.Status;

/**
 * this SoapMessage exposes the method of the Message which are needed in the SOAP interface.
 * 
 * @author tibi
 */
public class SoapMessage { 
	private String content;
	private Status status;
	private Long projectId;

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
