package ui.stats;

import java.awt.Paint;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import model.GlobalSummary;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ui.DateRangeNullable;
import ui.GuiController;

public class ConsultationPerPeriod extends AbstractSimpleDateRangeStat {

	private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#0.000");

	public ConsultationPerPeriod(GuiController controller) {
		super(controller);
		super.setActionListener(new ProcessListener());
		DEC_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
	}

	public String getName() {
		return "Diagramme de consulations";
	}

	List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listAllSummaryInRange(dateRange, null, null);
	}

	void processResult(List<GlobalSummary> summaryList) {
		resultPanel.removeAll();

		TimeSeriesCollection dataSet = AbstractDateRangeChartUtil.createTimeSeriesCollection(summaryList);
		JFreeChart chart = AbstractDateRangeChartUtil.createChart(dataSet, "Consultations", "Date", "Consultations", null);
		processRegression(chart.getXYPlot(), dataSet);
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);
		resultPanel.add(chartPanel);
		resultPanel.updateUI();
	}


	private void processRegression(XYPlot plot, TimeSeriesCollection data) {
		XYItemRenderer scatterRenderer = plot.getRenderer();
		StandardXYItemRenderer regressionRenderer = new StandardXYItemRenderer();
		regressionRenderer.setBaseSeriesVisibleInLegend(true);
		plot.setDataset(1, regress(data));
		plot.setRenderer(1, regressionRenderer);
		DrawingSupplier ds = plot.getDrawingSupplier();
		for (int i = 0; i < data.getSeriesCount(); i++) {
			Paint paint = ds.getNextPaint();
			scatterRenderer.setSeriesPaint(i, paint);
			regressionRenderer.setSeriesPaint(i, paint);
		}
	}

	private static XYDataset regress(TimeSeriesCollection data) {
		// Determine bounds
		double xMin = Double.MAX_VALUE, xMax = 0;
		for (int i = 0; i < data.getSeriesCount(); i++) {
			TimeSeries ser = data.getSeries(i);
			for (int j = 0; j < ser.getItemCount(); j++) {

				double x = ser.getTimePeriod(j).getFirstMillisecond();
				if (x < xMin) {
					xMin = x;
				}
				if (x > xMax) {
					xMax = x;
				}
			}
		}
		// Create 2-point series for each of the original series
		XYSeriesCollection coll = new XYSeriesCollection();
		for (int i = 0; i < data.getSeriesCount(); i++) {
			TimeSeries ser = data.getSeries(i);
			int n = ser.getItemCount();
			double sx = 0, sy = 0, sxx = 0, sxy = 0, syy = 0;
			for (int j = 0; j < n; j++) {
				double x = ser.getTimePeriod(j).getFirstMillisecond();
				double y = ser.getValue(j).doubleValue();
				sx += x;
				sy += y;
				sxx += x * x;
				sxy += x * y;
				syy += y * y;
			}
			double b = (n * sxy - sx * sy) / (n * sxx - sx * sx);
			double a = sy / n - b * sx / n;

			XYSeries regr = new XYSeries("Tendance sur la période: " + DEC_FORMAT.format(b * 1000 * 60 * 60 * 24 * 7)
					+ "\n");
			regr.add(xMin, a + b * xMin);
			regr.add(xMax, a + b * xMax);
			coll.addSeries(regr);
		}
		return coll;
	}

}
