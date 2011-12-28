package ui;

import java.util.Date;

import org.jfree.data.time.DateRange;

public class DateRangeNullable {

	private Date lower;

	private Date upper;

	private static final long serialVersionUID = -3595093026533322581L;

	public DateRangeNullable(Date lower, Date upper) {
		setLower(lower);
		setUpper(upper);
	}

	public DateRange getDateRange() {
		if (lower != null && upper != null) {
			return new DateRange(lower, upper);
		} else {
			return null;
		}
	}

	public Date getLowerDate() {
		return lower;
	}

	public final void setLower(Date lower) {
		this.lower = lower;
	}

	public Date getUpperDate() {
		return upper;
	}

	public final void setUpper(Date upper) {
		this.upper = upper;
	}

}
