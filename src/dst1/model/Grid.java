package dst1.model;

import java.math.BigDecimal;
import java.util.HashSet;
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
	
	private Set<Membership> membershipList = new HashSet<Membership>();
	
	private Set<Cluster> clusterList = new HashSet<Cluster>();

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

	@OneToMany(mappedBy = "pk.grid")
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Grid))
			return false;
		Grid other = (Grid) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
