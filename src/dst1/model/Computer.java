package dst1.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import dst1.db.interfaces.IEntity;
import dst1.validator.CPUs;

public class Computer implements IEntity<Long> {

	private static final long serialVersionUID = 1326696973057917601L;
	
	private Long		id;
	private String		name;
	private Integer	cpus;
	private String		location;
	private Date		creation;
	private Date		lastUpdate;
	
	private Cluster		cluster;
	
	private Set<Execution> executionList = new HashSet<Execution>();
	
	public Computer() {}

	public Computer(String name, Integer cpus, String location, Date creation,
			Date lastUpdate) {
		this.name = name;
		this.cpus = cpus;
		this.location = location;
		this.creation = creation;
		this.lastUpdate = lastUpdate;
	}

	public Long getId() {
		return id;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Set<Execution> getExecutionList() {
		return executionList;
	}

	public void setExecutionList(Set<Execution> executionList) {
		this.executionList = executionList;
	}

	@Size(min = 5, max = 25)
	public String getName() {
		return name;
	}

	@CPUs(min = 2, max = 8)
	public Integer getCpus() {
		return cpus;
	}

	@Pattern(regexp="[A-Z]{3}-[A-Z]{3}@[0-9]{4,}")
	public String getLocation() {
		return location;
	}

	@NotNull
	@Past
	public Date getCreation() {
		return creation;
	}

	@NotNull
	@Past
	public Date getLastUpdate() {
		return lastUpdate;
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
}
