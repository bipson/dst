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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "computers")
public class Computer {

	private static final long serialVersionUID = 1326696973057917601L;

	private Long id;
	private String name;
	private Integer cpus;
	private String location;
	private Date creation;
	private Date lastUpdate;

	private Cluster cluster;

	private Set<Execution> executionList = new HashSet<Execution>();

	public Computer() {
	}

	public Computer(String name, Integer cpus, String location, Date creation,
			Date lastUpdate) {
		this.name = name;
		this.cpus = cpus;
		this.location = location;
		this.creation = creation;
		this.lastUpdate = lastUpdate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "computer_id")
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Column(name = "cpus")
	public Integer getCpus() {
		return cpus;
	}

	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation")
	public Date getCreation() {
		return creation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "cluster_fk")
	public Cluster getCluster() {
		return cluster;
	}

	@ManyToMany(mappedBy = "computerList")
	public Set<Execution> getExecutionList() {
		return executionList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCpus(Integer cpus) {
		this.cpus = cpus;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public void setExecutionList(Set<Execution> executionList) {
		this.executionList = executionList;
	}

}
