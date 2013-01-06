package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;
import ui.DateRangeNullable;
import ui.GuiController;

public class TopTenIntroducer extends AbstractDateRangeStat {

	private static final Integer MAX = 10;

	private GuiController controller;

	public TopTenIntroducer(GuiController controller) {
		super();
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	public String getName() {
		return "Top 10 adressants";
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	private void processResult(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();
		List<Entry<Introducer, Integer>> entryList = sortResults(summaryList);
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

	private List<Entry<Introducer, Integer>> sortResults(List<GlobalSummary> summaryList) {
		Map<Introducer, Integer> introMap = new HashMap<Introducer, Integer>();
		for (GlobalSummary globalSummary : summaryList) {
			List<IntroducerSummary> introSummaryList = globalSummary.getIntroducerSummaryList();
			//The list can be empty if no IntroducerSummary exists for the week and if the globalSummary has not been
			//persisted yet.
			if (introSummaryList != null) {
				for (IntroducerSummary introducerSummary : introSummaryList) {
					Introducer introducer = introducerSummary.getIntroducer();
					if (!introMap.containsKey(introducer)) {
						introMap.put(introducer, 0);
					}
					Integer currentNbr = introMap.get(introducer);
					Integer nbr = introducerSummary.getConsultationsNbr();
					introMap.put(introducer, currentNbr + nbr);
				}
			}
		}
		Set<Entry<Introducer, Integer>> entrySet = introMap.entrySet();
		List<Entry<Introducer, Integer>> entryList = new ArrayList<Map.Entry<Introducer, Integer>>(entrySet);
		Collections.sort(entryList, new Comparator<Map.Entry<Introducer, Integer>>() {

			@Override
			public int compare(Entry<Introducer, Integer> o1, Entry<Introducer, Integer> o2) {

				return o2.getValue() - o1.getValue();
			}
		});
		return entryList;
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
