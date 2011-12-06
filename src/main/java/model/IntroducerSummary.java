package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class IntroducerSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

    @OneToOne
    @JoinColumn(name="introducer_fk")
	private Introducer introducer;

	private Integer consultationsNbr;

	
	public IntroducerSummary() {
		super();
	}

	public IntroducerSummary(Introducer introducer, Integer consultationsNbr) {
		super();
		this.introducer = introducer;
		this.consultationsNbr = consultationsNbr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Introducer getIntroducer() {
		return introducer;
	}

	public void setIntroducer(Introducer introducer) {
		this.introducer = introducer;
	}

	public Integer getConsultationsNbr() {
		return consultationsNbr;
	}

	public void setConsultationsNbr(Integer consultationsNbr) {
		this.consultationsNbr = consultationsNbr;
	}

	@Override
	public String toString() {
		return "IntroducerSummary [id=" + id + ", introducer=" + introducer
				+ ", consultationsNbr=" + consultationsNbr + "]";
	}

}
