package nl.bhit.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
		name = "Project")
public class Project {
	private Long id;
	private String name;
	private Set<String> messages;
	private Company company;

	public Project(){
		this.messages = new TreeSet<String>();
	}
	
	public Project(String name){
		this.name = name;
		this.messages = new TreeSet<String>();
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
		
	public void addMessage(String message){
		messages.add(message);
	}
	
	public void removeMessage(String message){
		messages.remove(message);
	}
	
}