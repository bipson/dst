package dst1.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User extends Person {	

	private static final long serialVersionUID = -2851676057977461463L;

	private String	username;
	private byte[]	password;
	private String	accountNo;
	private String	bankCode;
	
	private List<Job> jobList;
	
	private List<Membership> membershipList;
	
	public User(){}
	
	@Column(name="username", unique=true, nullable=false)
	public String getUsername() {
		return username;
	}

	@Column(name="password", length=128)
	public byte[] getPassword() {
		return password;
	}
	
	@Column(name="account_no", unique=true)
	public String getAccountNo() {
		return accountNo;
	}

	@Column(name="bank_code", unique=true)
	public String getBankCode() {
		return bankCode;
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
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
