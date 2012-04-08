package dst1.model;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

@Entity
@Table(name="persons")
@Inheritance(strategy=InheritanceType.JOINED)
public class Person implements IEntity<Long> {
	
	private static final long serialVersionUID = -8718361763284334560L;
	
	private Long	id;
	private String	firstName;
	private String	lastName;
	
	@Embedded
	private Address address;
	
	public Person(){}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="person_id")
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

	@Column(name="address")
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
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

	@Override
	public String toString() {
		return (getFirstName() + ' ' + getLastName());
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
		if (!(obj instanceof Person))
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
