package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;
import ui.DateRangeNullable;
import ui.GuiController;

public class AverageWeek extends AbstractDateRangeStat {

	public AverageWeek(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	public String getName() {
		return "Moyennes";
	}

	private List<GlobalSummary> processAllStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	private List<GlobalSummary> processStatsHolidays(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, true);
	}

	private List<GlobalSummary> processStatsNotHolidays(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, false);
	}

	private void processResult(List<GlobalSummary> summaryList, List<GlobalSummary> summaryListHolidays,
			List<GlobalSummary> summaryListNotHolidays) {

		resultPanel.removeAll();

		Double globalAverage = StatUtil.average(summaryList);
		Double holidaysAverage = StatUtil.average(summaryListHolidays);
		Double notHolidaysAverage = StatUtil.average(summaryListNotHolidays);

		Double globalRatioNewPatients = StatUtil.ratioNewPatients(summaryList);
		Double holidaysRatioNewPatients = StatUtil.ratioNewPatients(summaryListHolidays);
		Double notHolidaysRatioNewPatients = StatUtil.ratioNewPatients(summaryListNotHolidays);

		JLabel globalLabel = new JLabel();
		globalLabel.setText("Moyenne globale : " + twoDigitsFormat.format(globalAverage) + " dont "
				+ twoDigitsFormat.format(globalRatioNewPatients) + "% de nouveaux patients");
		resultPanel.add(globalLabel);

		JLabel holidayLabel = new JLabel();
		holidayLabel.setText("Moyenne des semaines de vacances scolaires : " + twoDigitsFormat.format(holidaysAverage)
				+ " dont " + twoDigitsFormat.format(holidaysRatioNewPatients) + "% de nouveaux patients");
		resultPanel.add(holidayLabel);

		JLabel notHolidayLabel = new JLabel();
		notHolidayLabel.setText("Moyenne hors vacances scolaires : " + twoDigitsFormat.format(notHolidaysAverage)
				+ " dont " + twoDigitsFormat.format(notHolidaysRatioNewPatients) + "% de nouveaux patients");
		resultPanel.add(notHolidayLabel);

		resultPanel.updateUI();

	}

	private class ProcessListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DateRangeNullable dateRange = dateRangePanel.getDateRange();
			List<GlobalSummary> summaryList = processAllStats(dateRange);
			List<GlobalSummary> summaryListHolidays = processStatsHolidays(dateRange);
			List<GlobalSummary> summaryListNotHolidays = processStatsNotHolidays(dateRange);
			processResult(summaryList, summaryListHolidays, summaryListNotHolidays);
		}

	}

}
