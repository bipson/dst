package dst1.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User extends Person {	

	private static final long serialVersionUID = -2851676057977461463L;

	private String		username;
	private byte[]		password;
	
	private List<Job> jobList;
	
	private List<Membership> membershipList;
	
	public User(){}
	
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
