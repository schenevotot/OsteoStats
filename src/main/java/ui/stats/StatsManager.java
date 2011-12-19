package ui.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class StatsManager {

	private List<Stat> statList;

	public StatsManager() {
		statList = new ArrayList<Stat>();
	}

	public void plugStat(Stat stat) {
		statList.add(stat);
	}

	public JPanel processStatsSummaryPanel() {
		
		DefaultComboBoxModel<Stat> model = new DefaultComboBoxModel<Stat>(
				statList.toArray(new Stat[statList.size()]));
		JComboBox<Stat> comboBoxStats = new JComboBox<Stat>(model);
		comboBoxStats.setRenderer(new StatComboBoxRenderer());
		comboBoxStats.addActionListener(new StatSelectedListener());
		
		
		JPanel panel = new JPanel();
		panel.add(comboBoxStats);
		
		return panel;
		
		
	}
	
	private class StatSelectedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<Stat> comboBoxStats = (JComboBox<Stat>)e.getSource();
			Stat stat = (Stat)comboBoxStats.getSelectedItem();
			
		}
		
	}
}
