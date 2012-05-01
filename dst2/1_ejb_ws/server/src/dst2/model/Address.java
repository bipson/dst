package dst2.model;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = -8251776869155303083L;
	private String	street;
	private String	city;
	private String	zipCode;
	
	public Address() {};

	public Address(String street, String city, String zipCode) {
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
		
	};
	
	@Column(name="street")
	public String getStreet() {
		return street;
	}
	
	@Column(name="city")
	public String getCity() {
		return city;
	}
	
	@Column(name="zip_code")
	public String getZipCode() {
		return zipCode;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String toString() {
		return (street +", "+ city +", "+ zipCode);
	}
}
