package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.GlobalSummary;
import ui.DateRangeNullable;
import ui.GuiController;

public abstract class AbstractSimpleDateRangeStat extends AbstractDateRangeStat {

	public AbstractSimpleDateRangeStat(GuiController controller) {
		super(controller);
	}

	/**
	 * A simple Listener which gets the dateRange from the dateRangePanel, Get
	 * the stats matching it and then process the stats.
	 * 
	 * @author Seb
	 * 
	 */
	protected class ProcessListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DateRangeNullable dateRange = dateRangePanel.getDateRange();
			List<GlobalSummary> summaryList = processStats(dateRange);
			processResult(summaryList);
		}

	}

	abstract List<GlobalSummary> processStats(DateRangeNullable dateRange);

	abstract void processResult(List<GlobalSummary> summaryList);

}
