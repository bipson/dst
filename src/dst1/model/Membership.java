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
	
	private MembershipPK pk;
	
	public Membership() {
		pk = new MembershipPK();
	}
	public Membership(Date registration, Double discount) {
		pk = new MembershipPK();
		this.registration = registration;
		this.discount = discount;
	}
	
	@EmbeddedId
	public MembershipPK getPk() {
		return pk;
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
		return this.pk.getUser();
	}
	
	@Transient
	public Grid getGrid() {
		return this.pk.getGrid();
	}

	public void setPk(MembershipPK pk) {
		this.pk = pk;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public void setUser(User user) {
		this.pk.setUser(user);
	}
	
	public void setGrid(Grid grid) {
		this.pk.setGrid(grid);
	}

	@Override
	public MembershipPK obtainKey() {
		return this.pk;
	}
	
	@Override
	public int hashCode() {
		
		return pk.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		
		return pk.equals(other);
	}
}
