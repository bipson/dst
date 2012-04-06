package dst1.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

@Entity
@Table(name="grids")
public class Grid implements IEntity<Long> {

	private static final long serialVersionUID = -3900584002751525776L;
	
	private Long		id;
	private String		name;
	private String		location;
	private BigDecimal	costsPerCPUMinute;
	
	private Set<Membership> membershipList;
	
	private Set<Cluster> clusterList;

	public Grid() {}
	
	public Grid(String name, String location, BigDecimal costsPerCPUMinute) {
		this.name = name;
		this.location = location;
		this.costsPerCPUMinute = costsPerCPUMinute;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="grid_id")
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

//	@OneToMany(mappedBy="membership")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.grid", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public Set<Membership> getMembershipList() {
		return membershipList;
	}

	@OneToMany(mappedBy="grid")
	public Set<Cluster> getClusterList() {
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

	public void setMembershipList(Set<Membership> membershipList) {
		this.membershipList = membershipList;
	}

	public void setClusterList(Set<Cluster> clusterList) {
		this.clusterList = clusterList;
	}

	@Override
	public Long obtainKey() {
		return id;
	}

}
