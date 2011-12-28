package ui.stats;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class StatsManager {

	private List<Stat> statList;
	private JPanel mainPanel;
	private JPanel displayPanel;

	public StatsManager() {
		statList = new ArrayList<Stat>();
	}

	public void plugStat(Stat stat) {
		statList.add(stat);
	}

	public JPanel processStatsSummaryPanel() {
		mainPanel = new JPanel(new BorderLayout());
		DefaultComboBoxModel<Stat> model = new DefaultComboBoxModel<Stat>(statList.toArray(new Stat[statList.size()]));
		JComboBox<Stat> comboBoxStats = new JComboBox<Stat>(model);
		comboBoxStats.setRenderer(new StatComboBoxRenderer());
		comboBoxStats.addItemListener(new StatSelectedListener());
		mainPanel.add(comboBoxStats, BorderLayout.NORTH);

		displayPanel = new JPanel(new CardLayout());
		for (Stat stat : statList) {
			displayPanel.add(stat.display(), stat.getName());
		}
		mainPanel.add(displayPanel, BorderLayout.CENTER);

		return mainPanel;

	}

	private class StatSelectedListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			Stat stat = (Stat) e.getItem();
			CardLayout cl = (CardLayout) displayPanel.getLayout();
			cl.show(displayPanel, stat.getName());
		}
	}
}
