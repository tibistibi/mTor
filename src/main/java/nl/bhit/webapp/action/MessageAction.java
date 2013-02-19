package nl.bhit.webapp.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.bhit.dao.SearchException;
import nl.bhit.model.MTorMessage;
import nl.bhit.model.Project;
import nl.bhit.model.Status;
import nl.bhit.model.User;
import nl.bhit.service.GenericManager;
import nl.bhit.service.MessageManager;
import nl.bhit.webapp.util.UserManagementUtils;

import com.opensymphony.xwork2.Preparable;

public class MessageAction extends BaseAction implements Preparable {
	private MessageManager messageManager;
	private GenericManager<Project, Long> projectManager;
	private List mTorMessages;
	private List projects;
	private List status;
	private MTorMessage message;
	private Long id;
	private String query;

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public void setProjectManager(GenericManager<Project, Long> projectManager) {
		this.projectManager = projectManager;
	}

	public List getMTorMessages() {
		return mTorMessages;
	}

	/**
	 * Grab the entity from the database before populating with request parameters
	 */
	@Override
	public void prepare() {
		if (getRequest().getMethod().equalsIgnoreCase("post")) {
			// prevent failures on new
			String messageId = getRequest().getParameter("message.id");
			if (messageId != null && !messageId.equals("")) {
				message = messageManager.get(new Long(messageId));
			}
		}
	}

	public void setQ(String q) {
		this.query = q;
	}

	public String list() {
		try {
			// messages = messageManager.search(query, Message.class);
			mTorMessages = new ArrayList();
			List<MTorMessage> tempMessages = messageManager.search(query, MTorMessage.class);
			List<Project> tempProjects = getProjectCompanyList();
			for (MTorMessage tempMessage : tempMessages) {
				String messageProjectName = tempMessage.getProject().getName();
				for (Project tempProject : tempProjects) {
					if (tempProject.getName().equalsIgnoreCase(messageProjectName)) {
						mTorMessages.add(tempMessage);
					}
				}
			}
			Collection messagesNew = new LinkedHashSet(mTorMessages);
			mTorMessages = new ArrayList(messagesNew);
		} catch (SearchException se) {
			addActionError(se.getMessage());
			mTorMessages = messageManager.getAllDistinct();
		}
		return SUCCESS;
	}

	public List getProjectCompanyList() {
		List<Project> tempProjects = projectManager.getAllDistinct();
		String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
		projects = new ArrayList();
		for (Project tempProject : tempProjects) {
			Set<User> projectUsers = tempProject.getUsers();
			for (User projectUser : projectUsers) {
				if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)) {
					projects.add(tempProject);
				}
			}
		}
		return projects;
	}

	public List getStatusList() {
		return Status.getAsList();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MTorMessage getMessage() {
		return message;
	}

	public void setMessage(MTorMessage message) {
		this.message = message;
	}

	public String delete() {
		messageManager.remove(message.getId());
		saveMessage(getText("message.deleted"));

		return SUCCESS;
	}

	public String edit() {
		if (id != null) {
			message = messageManager.get(id);
		} else {
			message = new MTorMessage();
		}

		return SUCCESS;
	}

	public String save() throws Exception {
		if (cancel != null) {
			return "cancel";
		}

		if (delete != null) {
			return delete();
		}

		boolean isNew = (message.getId() == null);

		messageManager.save(message);

		String key = (isNew) ? "message.added" : "message.updated";
		saveMessage(getText(key));

		if (!isNew) {
			return INPUT;
		}
		return SUCCESS;
	}

	public String resolve() {
		List<MTorMessage> mTorMessagesList = new ArrayList();
		mTorMessagesList = messageManager.getMessagesWithTimestamp(message);

		for (MTorMessage tempMessage : mTorMessagesList) {
			tempMessage.setResolved(true);
			messageManager.save(tempMessage);
		}
		return SUCCESS;
	}
}