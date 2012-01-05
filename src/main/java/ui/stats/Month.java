package ui.stats;

public class Month {

	private int year;
	private int monthInYear;
	private int value;

	public Month(int year, int month) {
		this.year = year;
		this.monthInYear = month;
	}

	public int getYear() {
		return year;
	}

	public int getMonthInYear() {
		return monthInYear;
	}

	public void addToValue(int val) {
		value += val;
	}

	public int getValue() {
		return value;
	}

}
