package ui.stats;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

public abstract class AbstractDateRangeChart extends AbstractDateRangeStat {

	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	public AbstractDateRangeChart() {
	}

	protected JFreeChart createChart(XYDataset dataSet, String title, String domainTitle, String rangeTitle, String dateFormat) {
		// create the chart...
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, // chart title
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
			axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));	
		}
		else {
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

}