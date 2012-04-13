package dst1.model;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((clusterList == null) ? 0 : clusterList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Admin))
			return false;
		Admin other = (Admin) obj;
		if (clusterList == null) {
			if (other.clusterList != null)
				return false;
		} else if (!clusterList.equals(other.clusterList))
			return false;
		return true;
	}
}
