package dst2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

//
//@NamedQueries({
//	@NamedQuery(name="User.find", query="SELECT DISTINCT u FROM User u JOIN u.membershipList m "+
//			"JOIN m.pk.grid g JOIN g.clusterList cluL JOIN cluL.computerList comL "+
//			"JOIN comL.executionList e JOIN e.job j "+
//			"WHERE m.pk.grid.name = :gridname "+
//			"AND "+
//			"((SELECT COUNT(j.id) FROM j GROUP BY u.id)) >= :jobcount"),
//	@NamedQuery(name="User.mostActive", query="SELECT u FROM User u "+
//			"WHERE SIZE(u.jobList) >= "+
//			"ALL(SELECT COUNT(joblist) FROM User s JOIN s.jobList joblist GROUP BY s.id)"),
//})


/*
@org.hibernate.annotations.Table( appliesTo = "users", indexes = {
      @org.hibernate.annotations.Index(name="pass", columnNames = "password"),
   }
)
*/
@Entity
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
		
		this.username = username;
		this.accountNo = accountNo;
		this.bankCode = bankCode;
		this.password = password;
	}
	
	@Column(name="username", unique=true, nullable=false)
	public String getUsername() {
		return username;
	}

	@Column(name="password", columnDefinition="VARCHAR(16)")
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

	@OneToMany(mappedBy = "user")
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
}
