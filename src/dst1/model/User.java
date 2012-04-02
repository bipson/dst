package dst1.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User implements Serializable {	

	private static final long serialVersionUID = -2851676057977461463L;

	private Long		id;
	private String		firstName;
	private String		lastName;
	private String		username;
	private byte[]		password;
	
	private List<Job> jobList;
	
	private List<Membership> membershipList;
	
	public User(){}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
	public Long getId() {
		return id;
	}
	
	@Column(name="firstname")
	public String getFirstName() {
		return firstName;
	}
	
	@Column(name="lastname")
	public String getLastName() {
		return lastName;
	}
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}

	@Column(name="username")
	public byte[] getPassword() {
		return password;
	}

	@OneToMany(mappedBy="job")
	public List<Job> getJobList() {
		return jobList;
	}

	@OneToMany(mappedBy="membership")
	public List<Membership> getMembershipList() {
		return membershipList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}
	

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public void setMembershipList(List<Membership> membershipList) {
		this.membershipList = membershipList;
	}
}
