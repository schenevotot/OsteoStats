package ui.stats;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GlobalSummary;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

import ui.DateRangeNullable;
import ui.DateRangePanel;
import ui.GuiController;
import util.DateUtil;

public class ComparativeDoubleDateRangeStat extends AbstractDateRangeStat {

	protected DateRangePanel otherDateRangePanel;

	public ComparativeDoubleDateRangeStat(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
	}

	@Override
	public String getName() {
		return "Comparaison de 2 périodes";
	}

	@Override
	public JPanel display() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		dateRangePanel = new DateRangePanel();
		otherDateRangePanel = new DateRangePanel();

		JPanel inputPanel = new JPanel();
		JLabel label1 = new JLabel("Période 1:");
		inputPanel.add(label1);
		inputPanel.add(dateRangePanel);
		JLabel label2 = new JLabel("Période 2:");
		inputPanel.add(label2);
		inputPanel.add(otherDateRangePanel);

		button = new JButton("OK");
		button.addActionListener(listener);
		inputPanel.add(button);
		panel.add(inputPanel, BorderLayout.NORTH);

		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		panel.add(resultPanel, BorderLayout.CENTER);

		return panel;
	}

	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, null, null);
	}

	private void processResult(List<GlobalSummary> summaryList1, List<GlobalSummary> summaryList2) {
		resultPanel.removeAll();

		processCharts(summaryList1, summaryList2);

		processStats(summaryList1);
		processStats(summaryList2);

		System.out.println(summaryList1);
		System.out.println(summaryList2);

	}

	private void processStats(List<GlobalSummary> summaryList) {
		Double average1 = StatUtil.average(summaryList);
		Double ratioNewPatients1 = StatUtil.ratioNewPatients(summaryList);

		JPanel statsPanel = new JPanel();
		JLabel averageLabel = new JLabel();

		averageLabel.setText("Moyenne : " + twoDigitsFormat.format(average1) + " dont "
				+ twoDigitsFormat.format(ratioNewPatients1) + "% de nouveaux patients");
		JLabel cumulativeLabel = new JLabel();
		int nbrConsultations = StatUtil.cumulativePatients(summaryList);
		GlobalSummary maxSummary = StatUtil.maximumNbrOfConsultations(summaryList);

		StringBuffer text = new StringBuffer("Nombre de consultations : " + nbrConsultations);

		if (maxSummary != null) {
			text.append(" Max : " + maxSummary.getTotalNbrConsultation());
			if (maxSummary.getWeek().getStartDate() != null) {
				text.append(" le " + DateUtil.dateFormat(maxSummary.getWeek().getStartDate()));
			}

		}
		cumulativeLabel.setText(text.toString());

		statsPanel.add(averageLabel);
		statsPanel.add(cumulativeLabel);
		resultPanel.add(statsPanel);
	}

	private void processCharts(List<GlobalSummary> summaryList1, List<GlobalSummary> summaryList2) {
		TimeSeriesCollection dataSet1 = AbstractDateRangeChartUtil.createTimeSeriesCollection(summaryList1);
		JFreeChart chart1 = AbstractDateRangeChartUtil.createChart(dataSet1, "Consultations", "Date", "Consultations",
				null);
		ChartPanel chartPanel1 = new ChartPanel(chart1, false);
		chartPanel1.setDomainZoomable(false);
		chartPanel1.setRangeZoomable(false);
		resultPanel.add(chartPanel1);
		TimeSeriesCollection dataSet2 = AbstractDateRangeChartUtil.createTimeSeriesCollection(summaryList2);
		JFreeChart chart2 = AbstractDateRangeChartUtil.createChart(dataSet2, "Consultations", "Date", "Consultations",
				null);
		ChartPanel chartPanel2 = new ChartPanel(chart2, false);
		chartPanel2.setDomainZoomable(false);
		chartPanel2.setRangeZoomable(false);
		resultPanel.add(chartPanel2);
		resultPanel.updateUI();
	}

	private class ProcessListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DateRangeNullable dateRange1 = dateRangePanel.getDateRange();
			DateRangeNullable dateRange2 = otherDateRangePanel.getDateRange();
			List<GlobalSummary> summaryList1 = processStats(dateRange1);
			List<GlobalSummary> summaryList2 = processStats(dateRange2);

			processResult(summaryList1, summaryList2);
		}

	}

}
