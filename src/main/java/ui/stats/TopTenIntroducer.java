package ui.stats;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JLabel;

import model.GlobalSummary;
import model.Introducer;
import ui.DateRangeNullable;
import ui.GuiController;

public class TopTenIntroducer extends AbstractSimpleDateRangeStat {

	private static final Integer MAX = 10;

	private IntroducerSorter sorter = new IntroducerSorter();

	public TopTenIntroducer(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	public String getName() {
		return "Top "+ MAX +" adressants";
	}

	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, true, null);
	}

	void processResult(List<GlobalSummary> summaryList) {
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

}
