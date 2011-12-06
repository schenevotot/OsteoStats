package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class GlobalSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="week_fk")
	private Week week;

	/**
	 * For each introducer, the number of patients introduced during the week.
	 */
	@ManyToMany
	@OrderBy
	private List<IntroducerSummary> introducerSummaryList;

	private Integer nbrConsultationSaturday;

	private Integer totalNbrConsultation;

	public GlobalSummary() {
		super();
	}
	
	public GlobalSummary(Week week,
			List<IntroducerSummary> introducerSummaryList,
			Integer nbrConsultationSaturday, Integer totalNbrConsultation) {
		super();
		this.week = week;
		this.introducerSummaryList = introducerSummaryList;
		this.nbrConsultationSaturday = nbrConsultationSaturday;
		this.totalNbrConsultation = totalNbrConsultation;
	}

	public Integer getNbrConsultationSaturday() {
		return nbrConsultationSaturday;
	}

	public void setNbrConsultationSaturday(Integer nbrConsultationSaturday) {
		this.nbrConsultationSaturday = nbrConsultationSaturday;
	}

	public Integer getTotalNbrConsultation() {
		return totalNbrConsultation;
	}

	public void setTotalNbrConsultation(Integer totalNbrConsultation) {
		this.totalNbrConsultation = totalNbrConsultation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<IntroducerSummary> getIntroducerSummaryList() {
		return introducerSummaryList;
	}

	public void setIntroducerSummaryList(List<IntroducerSummary> statsList) {
		this.introducerSummaryList = statsList;
	}

	public void addIntroducerSummary(IntroducerSummary introSummary) {
		if (this.introducerSummaryList == null) {
			this.introducerSummaryList = new ArrayList<IntroducerSummary>();
		}
		introducerSummaryList.add(introSummary);
	}
	
	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "GlobalSummary [id=" + id + ", week=" + week
				+ ", introducerSummaryList=" + introducerSummaryList
				+ ", nbrConsultationSaturday=" + nbrConsultationSaturday
				+ ", totalNbrConsultation=" + totalNbrConsultation + "]";
	}
}
