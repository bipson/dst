package dst1.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="memberships")
public class Membership  implements Serializable {

	private static final long serialVersionUID = -5890012313962061063L;

	private Date	registration;
	private Double	discount;
	
	private User user;
	
	private Grid grid;
	
	public Membership(){}

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
		return user;
	}

	@ManyToOne
	@JoinColumn(name="grid_fk")
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
}
