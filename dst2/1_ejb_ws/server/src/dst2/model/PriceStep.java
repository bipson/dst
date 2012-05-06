package dst2.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "price_steps")
public class PriceStep implements Comparable<PriceStep> {
	private Long id;
	private Integer numberOfHistoricalJobs;
	private BigDecimal price;
	
	public PriceStep() {
		super();
	}

	public PriceStep(Long id, Integer numberOfHistoricalJobs, BigDecimal price) {
		super();
		this.id = id;
		this.numberOfHistoricalJobs = numberOfHistoricalJobs;
		this.price = price;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "price_step_id")
	public Long getId() {
		return id;
	}

	@Column(name = "number_of_historical_jobs")
	public Integer getNumberOfHistoricalJobs() {
		return numberOfHistoricalJobs;
	}

	@Column(name = "price")
	public BigDecimal getPrice() {
		return price;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNumberOfHistoricalJobs(Integer numberOfHistoricalJobs) {
		this.numberOfHistoricalJobs = numberOfHistoricalJobs;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int compareTo(PriceStep other) {
		return (this.getNumberOfHistoricalJobs() - other.getNumberOfHistoricalJobs());
	}

}
