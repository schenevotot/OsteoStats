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
import model.Week;
import ui.DateRangeNullable;
import ui.DateRangePanel;
import ui.GuiController;
import util.DateUtil;

public class TopTenWeek extends AbstractStat {

	private static final Integer MAX = 10;

	private GuiController controller;

	private JPanel panel;

	private JButton button;

	private DateRangePanel dateRangePanel;

	private JPanel resultPanel;

	public TopTenWeek(GuiController controller) {
		this.controller = controller;
	}

	public String getName() {
		return "Top 10 semaines";
	}

	public JPanel display() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		dateRangePanel = new DateRangePanel();

		JPanel inputPanel = new JPanel();
		inputPanel.add(dateRangePanel);

		button = new JButton("OK");
		button.addActionListener(new ProcessListener());
		inputPanel.add(button);
		panel.add(inputPanel, BorderLayout.NORTH);

		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		panel.add(resultPanel, BorderLayout.CENTER);

		return panel;
	}

	private List<GlobalSummary> processStats(DateRangeNullable dateRange) {
		return controller.listNMaxSummaryInRange(dateRange, MAX);
	}

	private void process(List<GlobalSummary> summaryList) {

		resultPanel.removeAll();
		int i = 1;
		
		for (GlobalSummary globalSummary : summaryList) {
			JLabel label = new JLabel();
			Week week = globalSummary.getWeek();
			label.setText(i + ". " + "Semaine " + week.getWeekNbrInYear() + " du " + DateUtil.dateFormat(week.getStartDate()) + " : "
					+ globalSummary.getTotalNbrConsultation() + " consultations");
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
			process(summaryList);
		}

	}

}
