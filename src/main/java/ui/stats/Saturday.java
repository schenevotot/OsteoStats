package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;
import ui.DateRangeNullable;
import ui.GuiController;

public class Saturday extends AbstractDateRangeStat {

	private GuiController controller;

	public Saturday(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	public String getName() {
		return "Proportion de consulations le samedi";
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	private void processResult(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();
		JLabel label = new JLabel();
		label.setText("Proportion de consultations le samedi :" + saturdayPart(summaryList) * 100 + " %");
		resultPanel.add(label);
		resultPanel.updateUI();

	}

	private float saturdayPart(List<GlobalSummary> summaryList) {
		float saturdays = 0;
		float total = 0;
		for (GlobalSummary globalSummary : summaryList) {
			saturdays += globalSummary.getNbrConsultationSaturday();
			total += globalSummary.getTotalNbrConsultation();
		}
		return saturdays / total;
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
