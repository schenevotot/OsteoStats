package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Week {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	private Integer weekNbrInYear;

	private Date startDate;
	
	private Date endDate;

	private Boolean businessWeek;
	
	private Boolean schoolHolidaysWeek;

	public Week() {

	}
	

	public Week(Integer weekNbrInYear, Date startDate, Date endDate,
			Boolean businessWeek, Boolean schoolHolidaysWeek) {
		super();
		this.weekNbrInYear = weekNbrInYear;
		this.startDate = startDate;
		this.endDate = endDate;
		this.businessWeek = businessWeek;
		this.schoolHolidaysWeek = schoolHolidaysWeek;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getWeekNbrInYear() {
		return weekNbrInYear;
	}

	public void setWeekNbrInYear(Integer weekNbrInYear) {
		this.weekNbrInYear = weekNbrInYear;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getBusinessWeek() {
		return businessWeek;
	}

	public void setBusinessWeek(Boolean businessWeek) {
		this.businessWeek = businessWeek;
	}

	public Boolean getSchoolHolidaysWeek() {
		return schoolHolidaysWeek;
	}

	public void setSchoolHolidaysWeek(Boolean schoolHolidaysWeek) {
		this.schoolHolidaysWeek = schoolHolidaysWeek;
	}

	@Override
	public String toString() {
		return "Week [id=" + id + ", weekNbrInYear=" + weekNbrInYear
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", businessWeek=" + businessWeek + ", schoolHolidaysWeek="
				+ schoolHolidaysWeek + "]";
	}

}
