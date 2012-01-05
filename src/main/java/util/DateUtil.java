package util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	private static DateFormat dateFormatter = DateFormat.getDateInstance();

	public static boolean isValidWeekNbr(String text) {
		if (text != null) {
			int weekNbr = Integer.parseInt(text);
			if (weekNbr >= 0 && weekNbr < 54) {
				return true;
			}
		}
		return false;
	}

	public static Date getStartDateSuggestion() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DATE, -7);
		Date date = c.getTime();
		return date;
	}

	public static Date getEndDateSuggestion() {
		Calendar c = Calendar.getInstance();
		Date d = getStartDateSuggestion();
		c.setTime(d);
		c.add(Calendar.DATE, 7);

		return c.getTime();
	}

	public static int getWeekNbr(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	public static Date createStartDateFromWeekNbr(int weekNbr) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_YEAR, weekNbr);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}

	public static Date createEndDateFromWeekNbr(int weekNbr) {
		Calendar c = Calendar.getInstance();
		Date d = createStartDateFromWeekNbr(weekNbr);
		c.setTime(d);
		c.add(Calendar.DATE, 7);

		return c.getTime();
	}

	public static Date createEndDateFromStartDate(Date startDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, 7);

		return c.getTime();
	}

	public static Date createStartDateFromEndDate(Date endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.DATE, -7);

		return c.getTime();
	}

	public static boolean isMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}

	public static String dateFormat(Date date) {
		return dateFormatter.format(date);
	}

	public static int getMonthInYearForWeek(Date weekStart) {
		Calendar c = Calendar.getInstance();
		c.setTime(weekStart);
		c.add(Calendar.DATE, 2);

		return c.get(Calendar.MONTH);
	}

	public static int getYearForWeek(Date weekStart) {
		Calendar c = Calendar.getInstance();
		c.setTime(weekStart);
		c.add(Calendar.DATE, 2);

		return c.get(Calendar.YEAR);
	}

	public static String getMonthNameFromInt(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		return c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	}
}
