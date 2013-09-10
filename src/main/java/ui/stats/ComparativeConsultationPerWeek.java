package ui.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.GlobalSummary;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ui.DateRangeNullable;
import ui.GuiController;
import util.DateUtil;

public class ComparativeConsultationPerWeek extends AbstractDateRangeChart {

	public ComparativeConsultationPerWeek(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	public String getName() {
		return "Diagramme comparatif des consultations par semaine";
	}

	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, null, null);
	}

	void processResult(List<GlobalSummary> summaryList) {
		resultPanel.removeAll();

		Map<Integer, Collection<GlobalSummary>> summaryMap = orderSummariesByYear(summaryList);

		Set<Entry<Integer, Collection<GlobalSummary>>> entrySet = summaryMap.entrySet();
		List<TimeSeriesCollection> dataSetList = new ArrayList<TimeSeriesCollection>();
		for (Entry<Integer, Collection<GlobalSummary>> entry : entrySet) {
			TimeSeriesCollection dataSet = new TimeSeriesCollection();
			dataSet.setXPosition(TimePeriodAnchor.MIDDLE);
			Integer year = entry.getKey();
			Collection<GlobalSummary> col = entry.getValue();
			TimeSeries series = new TimeSeries("Consultations " + year);
			for (GlobalSummary summary : col) {
				org.jfree.data.time.Week week = new org.jfree.data.time.Week(summary.getWeek().getWeekNbrInYear(), year);
				series.add(week, summary.getTotalNbrConsultation());
			}
			dataSet.addSeries(series);
			dataSetList.add(dataSet);
		}

		JFreeChart chart = createChart(dataSetList, "Consultations", "Date", "Consultations", "dd-MMM");
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);
		resultPanel.add(chartPanel);
		resultPanel.updateUI();
	}

	private Map<Integer, Collection<GlobalSummary>> orderSummariesByYear(List<GlobalSummary> summaryList) {
		Map<Integer, Collection<GlobalSummary>> summaryMap = new LinkedHashMap<Integer, Collection<GlobalSummary>>();

		for (GlobalSummary summary : summaryList) {
			if (summary.getWeek().getStartDate() != null) {
				Integer year = DateUtil.getYearForWeek(summary.getWeek().getStartDate());
				if (!summaryMap.containsKey(year)) {
					summaryMap.put(year, new HashSet<GlobalSummary>());
				}
				Collection<GlobalSummary> col = summaryMap.get(year);
				col.add(summary);
			}
		}

		return summaryMap;
	}
}
