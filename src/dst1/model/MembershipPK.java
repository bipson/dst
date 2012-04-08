package dst1.model;

import java.io.Serializable;

import javax.persistence.*;

public class MembershipPK implements Serializable {

	private static final long serialVersionUID = 7575176832748301904L;
	
	private User user;
	private Grid grid;
	
	public MembershipPK(){}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	public User getUser() {
		return user;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	public Grid getGrid() {
		return grid;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MembershipPK))
			return false;
		MembershipPK other = (MembershipPK) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	

}
