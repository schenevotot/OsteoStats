package ui.stats;

import java.util.Date;
import java.util.List;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Week;
import ui.DateRangeNullable;
import ui.GuiController;
import util.DateUtil;

public class TopTenWeek extends AbstractSimpleDateRangeStat {

	private static final Integer MAX = 10;

	public TopTenWeek(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	public String getName() {
		return "Top 10 semaines";
	}

	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listNMaxSummaryInRange(dateRange, MAX, true);
	}

	void processResult(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();
		int i = 1;
		
		for (GlobalSummary globalSummary : summaryList) {
			JLabel label = new JLabel();
			Week week = globalSummary.getWeek();
			Date startDate = week.getStartDate();
			String startDateFormat = "???";
			if (startDate != null) {
				startDateFormat = DateUtil.dateFormat(week.getStartDate());
			}
			
			label.setText(i + ". " + "Semaine " + week.getWeekNbrInYear() + " du " + startDateFormat + " : "
					+ globalSummary.getTotalNbrConsultation() + " consultations");
			resultPanel.add(label);
			i++;
		}
		resultPanel.updateUI();

	}

}
