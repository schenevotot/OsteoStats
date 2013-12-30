package ui.stats;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import model.GlobalSummary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import util.DateUtil;

public final class AbstractDateRangeChartUtil {

	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

	private AbstractDateRangeChartUtil() {

	}

	protected static TimeSeriesCollection createTimeSeriesCollection(List<GlobalSummary> summaryList) {
		TimeSeriesCollection dataSet = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries("Consultations");
		for (GlobalSummary summary : summaryList) {
			if (summary.getWeek().getStartDate() != null) {
				org.jfree.data.time.Week week = new org.jfree.data.time.Week(summary.getWeek().getWeekNbrInYear(),
						DateUtil.getYearForWeek(summary.getWeek().getStartDate()));
				series.add(week, summary.getTotalNbrConsultation());
			}
		}
		dataSet.addSeries(series);
		return dataSet;
	}

	protected static JFreeChart createChart(XYDataset dataSet, String title, String domainTitle, String rangeTitle,
			String dateFormat) {
		// create the chart...
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, // chart
																		// title
				domainTitle, // domain axis label
				rangeTitle, // range axis label
				dataSet, // initial series
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// set chart background
		chart.setBackgroundPaint(Color.white);

		// set a few custom plot features
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(0xffffe0));
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);

		// set the plot's axes to display integers
		TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (dateFormat != null) {
			axis.setDateFormatOverride(new SimpleDateFormat(dateFormat, Locale.getDefault()));
		} else {
			axis.setDateFormatOverride(DEFAULT_DATE_FORMAT);
		}
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		range.setStandardTickUnits(ticks);

		// render shapes and lines
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);

		// set the renderer's stroke
		Stroke stroke = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		renderer.setBaseOutlineStroke(stroke);

		// label the points
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
				StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, format, format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);

		return chart;
	}

	protected static JFreeChart createChart(List<TimeSeriesCollection> dataSetList, String title, String domainTitle,
			String rangeTitle, String dateFormat) {

		// Y axis
		NumberAxis valueAxis = new NumberAxis(rangeTitle);
		TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
		valueAxis.setStandardTickUnits(ticks);
		// The plot
		XYPlot plot = new XYPlot(null, null, valueAxis, null);
		int i = 0;
		for (TimeSeriesCollection dataSet : dataSetList) {
			// Create an X axis, not visible
			DateAxis localDateAxis = new DateAxis(domainTitle);
			localDateAxis.setVisible(i == 0);
			localDateAxis.setDateFormatOverride(new SimpleDateFormat(dateFormat, Locale.getDefault()));

			// For the range of the axis to have a good superposition
			int year = ((org.jfree.data.time.Week) dataSet.getSeries(0).getTimePeriod(0)).getYearValue();
			localDateAxis.setMaximumDate(DateUtil.getLastDayOfYear(year));
			localDateAxis.setMinimumDate(DateUtil.getFirstDayOfYear(year));

			plot.setDomainAxis(i, localDateAxis);
			plot.setDataset(i, dataSet);
			plot.mapDatasetToDomainAxis(i, i);

			// Add a renderer
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setUseFillPaint(true);
			renderer.setBaseFillPaint(Color.white);
			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1}: {2}", new SimpleDateFormat(
					"MMM yyyy", Locale.getDefault()), new DecimalFormat("0")));

			// label the points
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(2);
			XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
					StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT, format, format);
			renderer.setBaseItemLabelGenerator(generator);
			renderer.setBaseItemLabelsVisible(true);
			// A nice dot for each point
			Stroke stroke = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
			renderer.setBaseOutlineStroke(stroke);
			plot.setRenderer(i, renderer);
			i++;

		}
		// Set one axis visible and change it
		plot.getDomainAxis().setVisible(true);
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		ChartFactory.getChartTheme().apply(chart);
		return chart;

	}

}