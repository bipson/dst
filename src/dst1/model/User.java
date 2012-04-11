package dst1.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.*;

@Entity
@NamedQueries({
	@NamedQuery(name="User.find", query="SELECT u FROM User u JOIN u.membershipList m "+
			"WHERE m.pk.grid.name = :gridname AND "+
			"SIZE(u.jobList) >= :jobcount"),
//	@NamedQuery(name="User.find2", query="SELECT u FROM User u JOIN u.membershipList m "+
//			"JOIN m.pk.grid g JOIN g.clusterList cluL JOIN cluL.computerList comL JOIN comL.executionList e "+
//			"JOIN e.job j "+
//			"WHERE m.pk.grid.name = :gridname AND "+
//			"SIZE(u.jobList) >= :jobcount"),
	@NamedQuery(name="User.find3", query="SELECT u FROM User u JOIN u.membershipList m "+
			"WHERE m.pk.grid.name = :gridname AND "+
			"(SELECT COUNT(j) FROM m JOIN m.pk.grid g JOIN g.clusterList cluL JOIN cluL.computerList comL JOIN comL.executionList e "+
			"JOIN e.job j GROUP BY u.id) >= :jobcount"),
	@NamedQuery(name="User.mostActive", query="SELECT u FROM User u "+
			"WHERE SIZE(u.jobList) >= "+
			"ALL(SELECT COUNT(joblist) FROM User s JOIN s.jobList joblist GROUP BY s.id)"),
})

@Table(name="users",
	uniqueConstraints = {@UniqueConstraint(columnNames={"account_no", "bank_code"})}
)
@PrimaryKeyJoinColumn
public class User extends Person {

	private static final long serialVersionUID = -2851676057977461463L;

	private String	username;
	private byte[]	password;
	private String	accountNo;
	private String	bankCode;
	
	private Set<Job> jobList = new HashSet<Job>();
	
	private Set<Membership> membershipList = new HashSet<Membership>();
	
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

	@OneToMany(mappedBy = "user", orphanRemoval=true)
	public Set<Job> getJobList() {
		return jobList;
	}

	@OneToMany(mappedBy = "pk.user")
	public Set<Membership> getMembershipList() {
		return membershipList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public void setJobList(Set<Job> jobList) {
		this.jobList = jobList;
	}

	public void setMembershipList(Set<Membership> membershipList) {
		this.membershipList = membershipList;
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((accountNo == null) ? 0 : accountNo.hashCode());
		result = prime * result
				+ ((bankCode == null) ? 0 : bankCode.hashCode());
		result = prime * result + Arrays.hashCode(password);
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (accountNo == null) {
			if (other.accountNo != null)
				return false;
		} else if (!accountNo.equals(other.accountNo))
			return false;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
