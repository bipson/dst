package dst2.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="memberships")
@AssociationOverrides({
	@AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
	@AssociationOverride(name = "pk.grid", joinColumns = @JoinColumn(name = "grid_id"))
})
public class Membership {
	
	private static final long serialVersionUID = 4294549842113372395L;

	private Long	id;
	private Date	registration;
	private Double	discount;
	
	private User user;
	private Grid grid;
	
	public Membership() {}
	
	public Membership(Date registration, Double discount) {
		this.registration = registration;
		this.discount = discount;
	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="membership_id")
	public Long getId() {
		return id;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="registration")
	public Date getRegistration() {
		return registration;
	}

	@Column(name="discount")
	public Double getDiscount() {
		return discount;
	}
	
	@ManyToOne
	@JoinColumn(name="user_fk")
	public User getUser() {
		return this.user;
	}
	
	@ManyToOne
	@JoinColumn(name="grid_fk")
	public Grid getGrid() {
		return this.grid;
	}

	public void setId(Long id) {
		this.id = id;
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
}
