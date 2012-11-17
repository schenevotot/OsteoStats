package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import ui.DateRangeNullable;
import ui.GuiController;

public class AverageWeek extends AbstractDateRangeStat {

	private GuiController controller;

	public AverageWeek(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
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

		Double globalAverage = average(summaryList);
		Double holidaysAverage = average(summaryListHolidays);
		Double notHolidaysAverage = average(summaryListNotHolidays);

		JLabel globalLabel = new JLabel();
		globalLabel.setText("Moyenne globale : " + globalAverage);
		resultPanel.add(globalLabel);

		JLabel holidayLabel = new JLabel();
		holidayLabel.setText("Moyenne des semaines de vacances scolaires : " + holidaysAverage);
		resultPanel.add(holidayLabel);

		JLabel notHolidayLabel = new JLabel();
		notHolidayLabel.setText("Moyenne hors vacances scolaires : " + notHolidaysAverage);
		resultPanel.add(notHolidayLabel);

		resultPanel.updateUI();

	}

	private Double average(List<GlobalSummary> summaryList) {
		double[] values = new double[summaryList.size()];
		double[] weights =  new double[summaryList.size()];
		int i = 0;
		for (GlobalSummary globalSummary : summaryList) {
			values[i]=Double.valueOf(globalSummary.getTotalNbrConsultation());
			weights[i] = Double.valueOf(globalSummary.getWeek().getBusinessWeek() / 100);
			i++;
		}
		Mean mean = new Mean();
		mean.evaluate(values, weights);
		return mean.evaluate(values, weights);
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
