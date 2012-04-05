package dst1.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;

@Embeddable
public class MembershipPK  implements Serializable {

	private static final long serialVersionUID = 7575176832748301904L;

	private Date	registration;
	private Double	discount;
	
	private User user;
	
	private Grid grid;
	
	public MembershipPK(){}

	@Column(name="registration")
	public Date getRegistration() {
		return registration;
	}

	@Column(name="discount")
	public Double getDiscount() {
		return discount;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	@ManyToOne
	public Grid getGrid() {
		return grid;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public int hashCode() {
		int result;
		
		result = getUser().hashCode();
		result = 29 * result + getGrid().hashCode();
		
		return result;
	}
	
	public boolean equals(Object other) {
       
		if (this == other)
        	return true;
        
		if (other == null)
			return false;
		
        if (!(other instanceof MembershipPK))
        	return false;

        final MembershipPK pk = (MembershipPK) other;
        
        if (getGrid() == null || pk.getGrid() == null)
        	return false;
        
        if (getUser() == null || pk.getUser() == null)
        	return false;

        if ( !pk.getUser().equals(getUser()) )
        	return false;
        
        if ( !pk.getGrid().equals(getUser()) )
        	return false;

        return true;
    }
}
