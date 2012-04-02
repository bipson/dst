package dst1.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="grids")
public class Grid implements Serializable {

	private static final long serialVersionUID = -3900584002751525776L;
	
	private Long		id;
	private String		name;
	private String		location;
	private BigDecimal	costsPerCPUMinute;
	
	private List<Membership> membershipList;
	
	private List<Cluster> clusterList;
	
	public Grid() {}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="execution_id")
	public Long getId() {
		return id;
	}

	@Column(name="name", unique=true)
	public String getName() {
		return name;
	}

	@Column(name="location")
	public String getLocation() {
		return location;
	}

	@Column(name="costs_per_cpu_minute")
	public BigDecimal getCostsPerCPUMinute() {
		return costsPerCPUMinute;
	}

	@OneToMany(mappedBy="membership")
	public List<Membership> getMembershipList() {
		return membershipList;
	}

	@OneToMany(mappedBy="cluster")
	public List<Cluster> getClusterList() {
		return clusterList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setCostsPerCPUMinute(BigDecimal costsPerCPUMinute) {
		this.costsPerCPUMinute = costsPerCPUMinute;
	}

	public void setMembershipList(List<Membership> membershipList) {
		this.membershipList = membershipList;
	}

	public void setClusterList(List<Cluster> clusterList) {
		this.clusterList = clusterList;
	}

}
