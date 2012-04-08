package dst1.model;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="clusters")
public class Cluster implements IEntity<Long> {
	
	private static final long serialVersionUID = 2307967057488454586L;

	private Long id;
	private String name;
	private Date lastService;
	private Date nextService;
	
	private Grid grid;
	
	private Admin admin;
	
	private Set<Computer> computerList = new HashSet<Computer>();
	
	private Set<Cluster> clusterChildren = new HashSet<Cluster>();
	
	private Set<Cluster> clusterParents = new HashSet<Cluster>();
	
	public Cluster(){}
	
	public Cluster(String name, Date lastService, Date nextService) {
		this.name = name;
		this.lastService = lastService;
		this.nextService = nextService;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="cluster_id")
	public Long getId() {
		return id;
	}

	@Column(name="name", unique=true)
	public String getName() {
		return name;
	}

	@Column(name="last_service")
	public Date getLastService() {
		return lastService;
	}

	@Column(name="next_service")
	public Date getNextService() {
		return nextService;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	public Grid getGrid() {
		return grid;
	}

	@ManyToOne(optional=false)
	public Admin getAdmin() {
		return admin;
	}
	
	@OneToMany
	@JoinColumn(name="cluster_fk")
	public Set<Computer> getComputerList() {
		return computerList;
	}

	@ManyToMany
	@JoinTable(
			name="cluster_children",
			joinColumns=@JoinColumn(name="parent_id"),
			inverseJoinColumns=@JoinColumn(name="child_id")
	)
	public Set<Cluster> getClusterChildren() {
		return clusterChildren;
	}

	@ManyToMany(mappedBy="clusterChildren")
	public Set<Cluster> getClusterParents() {
		return clusterParents;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLastService(Date lastService) {
		this.lastService = lastService;
	}

	public void setNextService(Date nextService) {
		this.nextService = nextService;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public void setComputerList(Set<Computer> computerList) {
		this.computerList = computerList;
	}

	public void setClusterChildren(Set<Cluster> clusterChildren) {
		this.clusterChildren = clusterChildren;
	}

	public void setClusterParents(Set<Cluster> clusterParents) {
		this.clusterParents = clusterParents;
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
		if (!(obj instanceof Cluster))
			return false;
		Cluster other = (Cluster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
