package dst2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "clusters")
public class Cluster {

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

	public Cluster() {
	}

	public Cluster(String name, Date lastService, Date nextService) {
		this.name = name;
		this.lastService = lastService;
		this.nextService = nextService;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cluster_id")
	public Long getId() {
		return id;
	}

	@Column(name = "name", unique = true)
	public String getName() {
		return name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_service")
	public Date getLastService() {
		return lastService;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "next_service")
	public Date getNextService() {
		return nextService;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "grid_fk")
	public Grid getGrid() {
		return grid;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "admin_fk")
	public Admin getAdmin() {
		return admin;
	}

	@OneToMany()
	@JoinColumn(name = "cluster_fk")
	public Set<Computer> getComputerList() {
		return computerList;
	}

	@ManyToMany
	@JoinTable(name = "cluster_children", joinColumns = @JoinColumn(name = "parent_id"), inverseJoinColumns = @JoinColumn(name = "child_id"))
	public Set<Cluster> getClusterChildren() {
		return clusterChildren;
	}

	@ManyToMany(mappedBy = "clusterChildren")
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
}
