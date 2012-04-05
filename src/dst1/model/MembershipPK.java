package dst1.model;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class MembershipPK implements Serializable {

	private static final long serialVersionUID = 7575176832748301904L;
	
	private User user;
	
	private Grid grid;
	
	public MembershipPK(){}

	@ManyToOne
	public User getUser() {
		return user;
	}

	@ManyToOne
	public Grid getGrid() {
		return grid;
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
        
        if (this.grid == null || pk.getGrid() == null)
        	return false;
        
        if (this.user == null || pk.getUser() == null)
        	return false;

        if ( !pk.getUser().equals(this.user) )
        	return false;
        
        if ( !pk.getGrid().equals(this.grid) )
        	return false;

        return true;
    }
}
