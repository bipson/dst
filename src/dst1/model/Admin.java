package dst1.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="admins")
public class Admin  implements Serializable {
	
	private static final long serialVersionUID = -8718361763284334560L;
	
	private Long	id;
	private String	firstName;
	private String	lastName;
	
	private Set<Cluster> clusterList;
	
	public Admin(){}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="admin_id")
	public Long getId() {
		return id;
	}
	
	@Column(name="firstname")
	public String getFirstName() {
		return firstName;
	}
	
	@Column(name="lastname")
	public String getLastName() {
		return lastName;
	}
	
	@OneToMany(mappedBy="cluster")
	public Set<Cluster> getClusterList() {
		return clusterList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	};
	
	public void setClusterList(Set<Cluster> clusterList) {
		this.clusterList = clusterList;
	}
}
