package nl.bhit.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
		name = "COMPANY")
public class Company {
	private Long id;
	private String name;
	private Set<Project> projects;
	
	public Company(){
		this.projects = new TreeSet<Project>();
	}
	
	public Company(String name){
		this.name = name;
		this.projects = new TreeSet<Project>();
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
	@JoinColumn(name = "COMPANY_FK")
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project){
		projects.add(project);
	}

	public void removeProject(Project project){
		projects.remove(project);
	}
}
