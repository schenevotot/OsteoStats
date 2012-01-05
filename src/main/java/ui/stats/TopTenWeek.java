package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Week;
import ui.DateRangeNullable;
import ui.GuiController;
import util.DateUtil;

public class TopTenWeek extends AbstractDateRangeStat {

	private static final Integer MAX = 10;

	private GuiController controller;

	public TopTenWeek(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	public String getName() {
		return "Top 10 semaines";
	}

	
	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listNMaxSummaryInRange(dateRange, MAX);
	}

	private void processResult(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();
		int i = 1;
		
		for (GlobalSummary globalSummary : summaryList) {
			JLabel label = new JLabel();
			Week week = globalSummary.getWeek();
			label.setText(i + ". " + "Semaine " + week.getWeekNbrInYear() + " du " + DateUtil.dateFormat(week.getStartDate()) + " : "
					+ globalSummary.getTotalNbrConsultation() + " consultations");
			resultPanel.add(label);
			i++;
		}
		resultPanel.updateUI();

	}

	private class ProcessListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DateRangeNullable dateRange = dateRangePanel.getDateRange();
			List<GlobalSummary> summaryList = processStats(dateRange);
			processResult(summaryList);
		}

	}

}
