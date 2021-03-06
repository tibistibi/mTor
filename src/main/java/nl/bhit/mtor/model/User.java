package nl.bhit.mtor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This class represents the basic "user" object in AppFuse that allows for authentication
 * and user management. It implements Acegi Security's UserDetails interface.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *         Extended to implement Acegi UserDetails interface
 *         by David Carter david@carter.net
 */
@Entity
@Table(
        name = "app_user")
@Indexed
@XmlRootElement
public class User extends BaseObject implements Serializable, UserDetails {
	
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String passwordHint;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String website;
    private Address address = new Address();
    private Integer version;
    private Set<Role> roles = new HashSet<Role>();
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private Set<Project> projects;
    private Status statusThreshold;
    private String qrToken;
    private Date qrTimestamp;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public User() {
    }

    /**
     * Create a new instance and set the username.
     * 
     * @param username
     *            login name for user.
     */
    public User(final String username) {
        this.username = username;
    }

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    @DocumentId
    public Long getId() {
        return id;
    }

    @Column(
            nullable = false,
            length = 50,
            unique = true)
    @Field
    public String getUsername() {
        return username;
    }

    @Column(
            nullable = false)
    @XmlTransient
    public String getPassword() {
        return password;
    }

    @Transient
    @XmlTransient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Column(
            name = "password_hint")
    @XmlTransient
    public String getPasswordHint() {
        return passwordHint;
    }

    @Column(
            name = "first_name",
            nullable = false,
            length = 50)
    @Field
    public String getFirstName() {
        return firstName;
    }

    @Column(
            name = "last_name",
            nullable = false,
            length = 50)
    @Field
    public String getLastName() {
        return lastName;
    }

    @Column(
            nullable = false,
            unique = true)
    @Field
    public String getEmail() {
        return email;
    }

    @Column(
            name = "phone_number")
    @Field(
            analyze = Analyze.NO)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Field
    public String getWebsite() {
        return website;
    }

    /**
     * Returns the full name.
     * 
     * @return firstName + ' ' + lastName
     */
    @Transient
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    @Embedded
    @IndexedEmbedded
    public Address getAddress() {
        return address;
    }

    @ManyToMany(
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(
                    name = "user_id") },
            inverseJoinColumns = @JoinColumn(
                    name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Convert user roles to LabelValue objects for convenience.
     * 
     * @return a list of LabelValue objects with role information
     */
    @Transient
    public List<LabelValue> getRoleList() {
        List<LabelValue> userRoles = new ArrayList<LabelValue>();

        if (this.roles != null) {
            for (Role role : roles) {
                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getName(), role.getName()));
            }
        }

        return userRoles;
    }

    /**
     * Adds a role for the user
     * 
     * @param role
     *            the fully instantiated role
     */
    public void addRole(Role role) {
    	if (getRoles() != null) {
    		getRoles().add(role);
        } else {
            Set<Role> setOfRoles = new HashSet<Role>();
            setOfRoles.add(role);
            setRoles(setOfRoles);
        }
    }
    
    /**
     * Adds and updates a set of roles for the user
     * 
     * @param roles
     * 				Set of fully instantiated roles
     */
    public void addRoles(Set<Role> roles) {
    	if (roles == null) {
    		return;
    	}
    	
    	if (roles.isEmpty()) {
    		if (getRoles() != null && !getRoles().isEmpty()) {
    			getRoles().clear();
    		}
    	} else {
    		Role roleAux = null;
    		Iterator<Role> itRoles = getRoles().iterator();
    		while (itRoles.hasNext()) {
    			roleAux = itRoles.next();
    			if (roles.contains(roleAux)) {
    				roles.remove(roleAux);
    			} else {
    				itRoles.remove();
    			}
    		}
    		for (final Role r : roles) {
    			addRole(r);
    		}
    	}
    }
    
    /**
     * Adds a project for the user
     * 
     * @param project
     *            the fully instantiated project
     */
    public void addProject(Project project) {
        if (getProjects() != null) {
            getProjects().add(project);
        } else {
            Set<Project> setOfProjects = new HashSet<Project>();
            setOfProjects.add(project);
            setProjects(setOfProjects);
        }
        project.addUser(this);
    }
    
    /**
     * Adds and updates a set of projects for the user
     * 
     * @param projects
     * 				Set of fully instantiated projects
     */
    public void addProjects(Set<Project> projects) {
    	if (projects == null) {
    		return;
    	}
    	
    	if (!projects.isEmpty()) {
    		for (final Project p : getProjects()) {
				p.removeUser(this);
    		}
    	}
    	getProjects().clear();
    	for (final Project p : projects) {
			addProject(p);
		}
    }

    /**
     * @return GrantedAuthority[] an array of roles.
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        authorities.addAll(roles);
        return authorities;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    @Column(
            name = "account_enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @Column(
            name = "account_expired",
            nullable = false)
    public boolean isAccountExpired() {
        return accountExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     * @return true if account is still active
     */
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Column(
            name = "account_locked",
            nullable = false)
    public boolean isAccountLocked() {
        return accountLocked;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     * @return false if account is locked
     */
    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Column(
            name = "credentials_expired",
            nullable = false)
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     * @return true if credentials haven't expired
     */
    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        final User user = (User)obj;
        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	final int prime = 31;
        return prime * (username == null ? 0 : username.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("username", this.username)
                .append("enabled", this.enabled).append("accountExpired", this.accountExpired)
                .append("credentialsExpired", this.credentialsExpired).append("accountLocked", this.accountLocked);

        if (roles != null) {
            sb.append("Granted Authorities: ");

            int i = 0;
            for (Role role : roles) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(role.toString());
                i++;
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString();
    }

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "users")
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<String> projectNames() {
        Set<String> projectNames = new HashSet<String>();
        if (getProjects() != null) {
            Set<Project> projectList = getProjects();
            for (Project project : projectList) {
                projectNames.add(project.getName());
            }
        }
        return projectNames;
    }

    @Column(
            name = "STATUS_THRESHOLD",
            length = 5)
    @Enumerated(EnumType.STRING)
    public Status getStatusThreshold() {
        return statusThreshold;
    }

    public void setStatusThreshold(Status statusThreshold) {
        this.statusThreshold = statusThreshold;
    }
    
    @Column(name="qr_token")
	public String getQrToken() {
		return qrToken;
	}

	public void setQrToken(String qrToken) {
		this.qrToken = qrToken;
	}
	
	@Column(name="qr_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getQrTimestamp() {
		return qrTimestamp;
	}

	public void setQrTimestamp(Date qrTimestamp) {
		this.qrTimestamp = qrTimestamp;
	}
	
}
