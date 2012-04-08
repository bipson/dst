package dst1.model;

import java.util.Date;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

@Entity
@Table(name="memberships")
@AssociationOverrides({
	@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
	@AssociationOverride(name = "pk.grid", joinColumns = @JoinColumn(name = "grid_id"))
})
public class Membership implements IEntity<MembershipPK> {
	
	private static final long serialVersionUID = 4294549842113372395L;

	private Date	registration;
	private Double	discount;
	
	private MembershipPK pk = new MembershipPK();
	
	public Membership() {}
	
	public Membership(Date registration, Double discount) {
		this.registration = registration;
		this.discount = discount;
	}
	
	@EmbeddedId
	private MembershipPK getPk() {
		return pk;
	}
	
	@SuppressWarnings("unused")
	private void setPk(MembershipPK pk) {
		this.pk = pk;
	}
	
	@Column(name="registration")
	public Date getRegistration() {
		return registration;
	}

	@Column(name="discount")
	public Double getDiscount() {
		return discount;
	}
	
	@Transient
	public User getUser() {
		return getPk().getUser();
	}
	
	@Transient
	public Grid getGrid() {
		return getPk().getGrid();
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public void setUser(User user) {
		getPk().setUser(user);
	}
	
	public void setGrid(Grid grid) {
		getPk().setGrid(grid);
	}
	
	@Override
	public int hashCode() {
		return (getPk() == null ? 0 : getPk().hashCode());
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
        if (other == null || !(other instanceof Membership)) return false;
 
        Membership that = (Membership) other;
 
        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;
 
        return true;
	}
}
