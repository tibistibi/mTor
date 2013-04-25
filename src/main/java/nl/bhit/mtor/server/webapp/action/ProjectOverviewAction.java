package nl.bhit.mtor.server.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.bhit.mtor.Constants;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.MessageManager;
import nl.bhit.mtor.service.ProjectManager;

import com.opensymphony.xwork2.Preparable;

public class ProjectOverviewAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2922415603733698707L;
	
	private transient ProjectManager projectManager;
	private transient MessageManager messageManager;
	
	private List<Project> projects = new ArrayList<Project>();
	
	private List<Map<String, String>> jsonGeneralProjectsInfo = new ArrayList<Map<String,String>>();
	private Map<String, String> jsonProjectStats = new HashMap<String, String>();
	
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

	@Override
	public void prepare() throws Exception {
		projects.clear();
        if (getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
        	setProjects(projectManager.getAll());        	
        } else {
        	User user = userManager.getUserByUsername(getRequest().getRemoteUser());
        	setProjects(new ArrayList<Project>(user.getProjects()));
        }
	}
	
	public String generalProjectsJSONInfo() {
		if (jsonGeneralProjectsInfo != null) {
			jsonGeneralProjectsInfo.clear();
		}
		if (projects.isEmpty()) {
			return SUCCESS;
		}
		
		jsonGeneralProjectsInfo = new ArrayList<Map<String,String>>(projects.size());
		Long numAux;
		MTorMessage newestMsg;
		Map<String, String> auxMap;
		for (final Project p : projects) {
			auxMap = new HashMap<String, String>();
			auxMap.put("name", p.getName());
			numAux = messageManager.getMessageNumber(p, Status.INFO);
			auxMap.put("info", numAux == null ? "-" : String.valueOf(numAux));
			numAux = messageManager.getMessageNumber(p, Status.WARN);
			auxMap.put("warn", numAux == null ? "-" : String.valueOf(numAux));
			numAux = messageManager.getMessageNumber(p, Status.ERROR);
			auxMap.put("error", numAux == null ? "-" : String.valueOf(numAux));
			newestMsg = messageManager.getNewestMessage(p);
			auxMap.put("newest", newestMsg == null ? "..." : newestMsg.getContent());
			
			jsonGeneralProjectsInfo.add(auxMap);
		}
		
		return SUCCESS;
	}
	
	public String projectJSONStats() {
		String projectId = getRequest().getParameter("project.id");
		if (projectId != null) {
			Project pStats = projectManager.get(Long.valueOf(projectId));
			Long nErrorMsg = messageManager.getMessageNumber(pStats, Status.ERROR);
			Long nInfoMsg = messageManager.getMessageNumber(pStats, Status.INFO);
			Long nNoneMsg = messageManager.getMessageNumber(pStats, Status.NONE);
			Long nWarnMsg = messageManager.getMessageNumber(pStats, Status.WARN);
			
			jsonProjectStats.put("error", String.valueOf(nErrorMsg));
			jsonProjectStats.put("info", String.valueOf(nInfoMsg));
			jsonProjectStats.put("none", String.valueOf(nNoneMsg));
			jsonProjectStats.put("warn", String.valueOf(nWarnMsg));
		}
		
		return SUCCESS;
	}

	/*
	 * Getters & Setters
	 */
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public List<Map<String, String>> getJsonGeneralProjectsInfo() {
		return jsonGeneralProjectsInfo;
	}
	
	public void setJsonGeneralProjectsInfo(List<Map<String, String>> jsonGeneralProjectsInfo) {
		this.jsonGeneralProjectsInfo = jsonGeneralProjectsInfo;
	}

	public Map<String, String> getJsonProjectStats() {
		return jsonProjectStats;
	}

	public void setJsonProjectStats(Map<String, String> jsonProjectStats) {
		this.jsonProjectStats = jsonProjectStats;
	}
	
}
