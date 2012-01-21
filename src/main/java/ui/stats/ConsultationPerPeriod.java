package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.GlobalSummary;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import ui.DateRangeNullable;
import ui.GuiController;
import util.DateUtil;

public class ConsultationPerPeriod extends AbstractDateRangeChart {

	private GuiController controller;

	public ConsultationPerPeriod(GuiController controller) {
		super.setActionListener(new ProcessListener());
		this.controller = controller;
	}

	public String getName() {
		return "Diagramme de consulations";
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, null, null);
	}

	private void processResult(List<GlobalSummary> summaryList) {
		resultPanel.removeAll();

		TimeSeriesCollection dataSet = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries("Consultations");
		int i = 0;
		for (GlobalSummary summary : summaryList) {
			org.jfree.data.time.Week week = new org.jfree.data.time.Week(summary.getWeek().getWeekNbrInYear(),
					DateUtil.getYearForWeek(summary.getWeek().getStartDate()));
			series.add(week, summary.getTotalNbrConsultation());
			i++;
		}
		dataSet.addSeries(series);
		JFreeChart chart = createChart(dataSet, "Consultations", "Date", "Consultations", null);
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);
		resultPanel.add(chartPanel);
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
