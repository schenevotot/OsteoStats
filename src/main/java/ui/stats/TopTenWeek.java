package ui.stats;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GlobalSummary;

import org.jfree.data.time.DateRange;

import ui.GuiController;

public class TopTenWeek {

	private static final Integer MAX = 10;

	private GuiController controller;

	public TopTenWeek(GuiController controller) {
		this.controller = controller;
	}

	public String getName() {
		return "Top 10 semaines";
	}

	private List<GlobalSummary> processStats(DateRange dateRange) {
		return controller.listNMaxSummaryInRange(dateRange, MAX);
	}

	public JPanel process(DateRange dateRange) {
		List<GlobalSummary> summaryList = processStats(dateRange);
		JPanel panel = new JPanel();

		JLabel label = new JLabel();
		panel.add(label);
		for (GlobalSummary globalSummary : summaryList) {
			label.setText(label.getText() + "<br/>" + globalSummary.getWeek().getWeekNbrInYear() + ": "
					+ globalSummary.getTotalNbrConsultation());
		}
		return panel;
	}

}
