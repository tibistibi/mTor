package nl.bhit.mtor.model;

import java.io.Serializable;
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
public class Company extends BaseObject implements Serializable {

    private static final long serialVersionUID = 7382525403256174201L;
    private Long id;
    private String name;
    private Set<Project> projects;

    public Company() {
        this.projects = new TreeSet<Project>();
    }

    public Company(String name) {
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

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(
            name = "COMPANY_FK")
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
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
        Company other = (Company) obj;
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
        return "Company [id=" + id + ", name=" + name + "]";
    }
}
