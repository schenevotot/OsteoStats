package ui.stats;

import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;
import ui.DateRangeNullable;
import ui.GuiController;

public class Saturday extends AbstractSimpleDateRangeStat {

	public Saturday(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	public String getName() {
		return "Proportion de consulations le samedi";
	}
	
	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	void processResult(List<GlobalSummary> summaryList) {

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



}
