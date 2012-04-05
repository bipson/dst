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
	public Long obtainKey() {
		return this.id;
	}

	@Override
	public String toString() {
		return (getFirstName() + ' ' + getLastName());
	}
}
