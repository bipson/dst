package dst1.model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="admins")
@PrimaryKeyJoinColumn(name="person")
public class Admin extends Person {
	
	private static final long serialVersionUID = -8718361763284334560L;
	
	private Set<Cluster> clusterList;
	
	public Admin(){}
	
	@OneToMany(mappedBy="cluster")
	public Set<Cluster> getClusterList() {
		return clusterList;
	}

	public void setClusterList(Set<Cluster> clusterList) {
		this.clusterList = clusterList;
	}
}
