package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Introducer;
import ui.DateRangeNullable;
import ui.GuiController;

public class TopTenIntroducer extends AbstractDateRangeStat {

	private static final Integer MAX = 10;

	private GuiController controller;
	
	private IntroducerSorter sorter = new IntroducerSorter();

	public TopTenIntroducer(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	public String getName() {
		return "Top "+ MAX +" adressants";
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	private void processResult(List<GlobalSummary> summaryList) {
		resultPanel.removeAll();

		List<Entry<Introducer, Integer>> entryList = sorter.sortResults(summaryList);
		int i = 1;
		for (Iterator<Entry<Introducer, Integer>> iterator = entryList.iterator(); iterator.hasNext() && i <= MAX;) {
			Entry<Introducer, Integer> entry = iterator.next();
			JLabel label = new JLabel();
			Introducer intro = entry.getKey();
			Integer nbr = entry.getValue();
			label.setText(i + ". " + intro.getName() + " avec " + nbr + " consulations");
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
