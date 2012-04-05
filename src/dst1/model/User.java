package dst1.model;

import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.*;

@Entity
@Table(name="users",
	uniqueConstraints = {@UniqueConstraint(columnNames={"account_no", "bank_code"})}
)
public class User extends Person {	

	private static final long serialVersionUID = -2851676057977461463L;

	private String	username;
	private byte[]	password;
	private String	accountNo;
	private String	bankCode;
	
	private List<Job> jobList;
	
	private List<Membership> membershipList;
	
	public User(){}
	
	public User(String username, byte[] password, String accountNo, String bankCode) {
		MessageDigest md = null;
		
		this.username = username;
		this.accountNo = accountNo;
		this.bankCode = bankCode;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
		this.password = md.digest(password);
	}
	
	@Column(name="username", unique=true, nullable=false)
	public String getUsername() {
		return username;
	}

	@Column(name="password", length=16)
	public byte[] getPassword() {
		return password;
	}
	
	@Column(name="account_no")
	public String getAccountNo() {
		return accountNo;
	}

	@Column(name="bank_code")
	public String getBankCode() {
		return bankCode;
	}

	@OneToMany(mappedBy="user")
	public List<Job> getJobList() {
		return jobList;
	}

//	@OneToMany(mappedBy="membership")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user", cascade =
		{CascadeType.PERSIST, CascadeType.MERGE})
	public List<Membership> getMembershipList() {
		return membershipList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(byte[] password) {
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
		this.password = md.digest(password);
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
