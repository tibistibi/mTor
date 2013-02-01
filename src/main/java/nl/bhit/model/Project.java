package nl.bhit.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
 
@Entity
@Table(
		name = "PROJECT")
public class Project {
	private Long id;
	private String name;
	private Set<MTorMessage> messages;
	private Company company;
	private Set<User> users;


	public Project(){
		this.messages = new TreeSet<MTorMessage>();
	}
	
	public Project(String name){
		this.name = name;
		this.messages = new TreeSet<MTorMessage>();
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPANY_FK")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
			name = "NAME",
			unique = true,
			nullable = false)	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(fetch = FetchType.EAGER,
			   cascade = CascadeType.ALL)
	@JoinColumn(name = "PROJECT_FK")
	public Set<MTorMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<MTorMessage> messages) {
		this.messages = messages;
	}
	
	public void addMessage(MTorMessage message){
		messages.add(message);
	}
	
	public void removeMessage(String message){
		messages.remove(message);
	}
	
	

	public  String statusOfProject() {
		Set<MTorMessage> currentMessages= getMessages();
		if(!currentMessages.isEmpty()){
			for (MTorMessage message : currentMessages) { 
				Status status = message.getStatus();
				if(status.equals(Status.ERROR)){
					return Status.ERROR.toString();
				} 
			}
			for (MTorMessage message : currentMessages) { 
				Status status = message.getStatus();
				if(status.equals(Status.WARN)){
					return Status.WARN.toString();						
				} 
			}						
		}
		return Status.INFO.toString();
	}
	
	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	 @JoinTable(name = "project_app_user",
	 joinColumns = {
	 @JoinColumn(name="PROJECT_ID") 
	 },
	 inverseJoinColumns = {
	 @JoinColumn(name="users_id")
	 }
	 )
	public Set<User> getUsers() {
		return users;
	}
	
	public Set<String> userNames() {
		Set<String> userNames = new HashSet<String>();
		if (getUsers()!=null) {
			Set<User> userList = getUsers();
			 for (User user : userList) {
				 userNames.add(user.getFullName());
			 }
		 } 
		 return userNames;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
    public void addUser(User user) {
    	if (getUsers()!=null) {
        getUsers().add(user);
    	} else {
    		Set<User> setOfUsers = new HashSet<User>();
    		setOfUsers.add(user);
    		setUsers(setOfUsers);
    	}
    }
}