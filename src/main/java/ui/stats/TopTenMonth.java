package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Week;
import ui.DateRangeNullable;
import ui.GuiController;
import util.DateUtil;

public class TopTenMonth extends AbstractDateRangeStat {

	private static final int MAX = 10;

	private GuiController controller;

	public TopTenMonth(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	@Override
	public String getName() {
		return "Top 10 mois";
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	private void processResult(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();

		int i = 1;
		List<Month> monthList = aggregate(summaryList);
		for (Iterator<Month> iterator = monthList.iterator(); iterator.hasNext() && i <= MAX;) {
			Month month = iterator.next();
			JLabel label = new JLabel();

			label.setText(i + ". " + "Mois de " + DateUtil.getMonthNameFromInt(month.getMonthInYear()) + " " + month.getYear() + " : "
					+ month.getValue() + " consultations");
			resultPanel.add(label);
			i++;
		}
		resultPanel.updateUI();
	}

	private List<Month> aggregate(List<GlobalSummary> summaryList) {

		Map<String, Month> monthMap = new HashMap<String, Month>();

		for (GlobalSummary globalSummary : summaryList) {
			Week week = globalSummary.getWeek();
			Date start = week.getStartDate();
			int monthNbr = DateUtil.getMonthInYearForWeek(start);
			int yearNbr = DateUtil.getYearForWeek(start);
			StringBuilder key = new StringBuilder();
			key.append(monthNbr);
			key.append(" - ");
			key.append(yearNbr);

			Month month = null;
			if (monthMap.containsKey(key.toString())) {
				month = monthMap.get(key.toString());
			} else {
				month = new Month(yearNbr, monthNbr);
				monthMap.put(key.toString(), month);
			}
			month.addToValue(globalSummary.getTotalNbrConsultation());
		}
		List<Month> monthList = new ArrayList<Month>(monthMap.values());

		Collections.sort(monthList, new Comparator<Month>() {

			@Override
			public int compare(Month o1, Month o2) {
				return o2.getValue() - o1.getValue();
			}
		});

		return monthList;
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
