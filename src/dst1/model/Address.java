package dst1.model;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
@Table(name="addresses")
public class Address  implements Serializable {

	private static final long serialVersionUID = -8251776869155303083L;
	private String	street;
	private String	city;
	private String	zipCode;
	
	public Address() {};
	
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
}
