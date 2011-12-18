package ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Introducer;

public class IntroducerComboBoxRenderer extends JLabel implements
ListCellRenderer<Introducer> {

	private static final long serialVersionUID = 1L;

	public IntroducerComboBoxRenderer() {
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Introducer> list, Introducer value, int index,
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
