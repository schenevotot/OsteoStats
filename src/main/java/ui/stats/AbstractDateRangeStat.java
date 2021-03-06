package ui.stats;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import ui.DateRangePanel;
import ui.GuiController;

/**
 * An abstract stat with a dateRangePanel.
 * 
 * @author Seb
 * 
 */
public abstract class AbstractDateRangeStat extends AbstractStat {

	protected GuiController controller;
	protected JPanel panel;
	protected JButton button;
	protected DateRangePanel dateRangePanel;
	protected JPanel resultPanel;
	protected ActionListener listener;
	
	public AbstractDateRangeStat(GuiController controller) {
		this.controller = controller;
	}

	protected void setActionListener(ActionListener listener) {
		this.listener = listener;
	}

	public JPanel display() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		dateRangePanel = new DateRangePanel();

		JPanel inputPanel = new JPanel();
		inputPanel.add(dateRangePanel);

		button = new JButton("OK");
		button.addActionListener(listener);
		inputPanel.add(button);
		panel.add(inputPanel, BorderLayout.NORTH);

		resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		panel.add(resultPanel, BorderLayout.CENTER);

		return panel;
	}
	
	

}
