package ui.stats;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class StatComboBoxRenderer extends JLabel implements
ListCellRenderer<Stat> {

	private static final long serialVersionUID = 1L;

	public StatComboBoxRenderer() {
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Stat> list, Stat value, int index,
			boolean isSelected, boolean cellHasFocus) {

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (value != null) {
			setText(value.getName());
		}
		return this;
	}

}
