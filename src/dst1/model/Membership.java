package dst1.model;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="memberships")
@AssociationOverrides({
	@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
	@AssociationOverride(name = "pk.grid", joinColumns = @JoinColumn(name = "grid_id"))
})
public class Membership {
	
	private MembershipPK pk = new MembershipPK();
	
	@EmbeddedId
	public MembershipPK getPk() {
		return pk;
	}

	public void setPk(MembershipPK pk) {
		this.pk = pk;
	}

	@Transient
	public Date getRegistration() {
		return getPk().getRegistration();
	}

	@Transient
	public Double getDiscount() {
		return getPk().getDiscount();
	}

	public void setRegistration(Date registration) {
		getPk().setRegistration(registration);
	}

	public void setDiscount(Double discount) {
		getPk().setDiscount(discount);
	}
}
