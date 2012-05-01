package dst2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="admins")
@PrimaryKeyJoinColumn
public class Admin extends Person {
	
	private static final long serialVersionUID = -8718361763284334560L;
	
	private Set<Cluster> clusterList = new HashSet<Cluster>();
	
	public Admin(){}
	
	@OneToMany(mappedBy="admin")
	public Set<Cluster> getClusterList() {
		return clusterList;
	}

	public void setClusterList(Set<Cluster> clusterList) {
		this.clusterList = clusterList;
	}
}
