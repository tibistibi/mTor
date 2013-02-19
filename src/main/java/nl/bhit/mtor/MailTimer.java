package nl.bhit.mtor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.bhit.dao.SearchException;
import nl.bhit.dao.hibernate.GenericDaoHibernate;
import nl.bhit.model.MTorMessage;
import nl.bhit.model.Project;
import nl.bhit.model.Status;
import nl.bhit.model.User;
import nl.bhit.service.GenericManager;
import nl.bhit.webapp.action.MailSendAction;
import nl.bhit.webapp.util.UserManagementUtils;


@Component
public class MailTimer {
	@Autowired
    private GenericManager<Project, Long> projectManager;
    private List<Project> projects;
 
    @Autowired
    public void setProjectManager(GenericManager<Project, Long> projectManager) {
        this.projectManager = projectManager;
    }
	public void process() {

		listOfProjects();
		for(Project project : projects){
			MailSendAction msa = new MailSendAction();		
			ArrayList<String> emailsTo = new ArrayList<String>();
			for (User user : project.getUsers()){
				emailsTo.add(user.getEmail());
			}
			
			Set<MTorMessage> currentMessages= project.getMessages();
			if(!currentMessages.isEmpty()){
				boolean isAlive = false;
				for (MTorMessage message : currentMessages) {
					long timestamp = message.getTimestamp().getTime();
					long currentTime = new Date().getTime();
					long difference = currentTime - timestamp;
					if (difference <= project.INTERVAL && message.getContent().contains("alive")){
						isAlive = true;
					}
				}
				if (!isAlive){
					msa.execute( emailsTo.toArray(), "mtor.bhit@gmail.com", 
							"Application is not alive - Error", 
							"In the last 5 minutes there is no IAmAlive message sent!");					
				}
				for (MTorMessage message : currentMessages) { 
					Status status = message.getStatus();
					if(status.equals(Status.ERROR) && !message.isResolved()){
						msa.execute( emailsTo.toArray(), "mtor.bhit@gmail.com", 
								"Project is in status Error, an error message has arrived", 
								message.getContent());
					}
				}
				for (MTorMessage message : currentMessages) { 
					Status status = message.getStatus();
					if(status.equals(Status.WARN) && !message.isResolved()){
						msa.execute( emailsTo.toArray(), "mtor.bhit@gmail.com", 
								"Project is in status Warning, a warning message has arrived", 
								message.getContent());
					}
				}						
			}
		}
	}
	
	public void listOfProjects(){
		 try {			   			    
	            Collection<Project> projectsNew = new LinkedHashSet<Project>(projectManager.search(null, Project.class));
	            List<Project> tempProjects = new ArrayList<Project>(projectsNew);
	            String loggedInUser = UserManagementUtils.getAuthenticatedUser().getFullName();
	            projects = new ArrayList<Project>();
	            for(Project tempProject : tempProjects){
	            	Set<User> projectUsers = tempProject.getUsers();
	            	for (User projectUser : projectUsers){
	            		if (projectUser.getFullName().equalsIgnoreCase(loggedInUser)){
	            			projects.add(tempProject);
	            		}
	            	}
	            }
	        } catch (SearchException se) {
	            projects = projectManager.getAllDistinct();
	        }
	}
}
