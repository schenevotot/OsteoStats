package ui;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JTextField;

import com.toedter.calendar.JTextFieldDateEditor;

public class BugFixJTextFieldDateEditor extends JTextFieldDateEditor {

	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getPreferredSize() {
		// if we have a date and a formatter, use that to get the text field
		// size
		if (this.dateFormatter != null) {
			// if no date, use today
			Date d = this.date != null ? this.date : new Date();
			String s = this.dateFormatter.format(d);
			return new JTextField(s).getPreferredSize();
		}
		// else, use the date pattern string itself
		if (datePattern != null) {
			return new JTextField(datePattern).getPreferredSize();
		}
		// else, return default
		return super.getPreferredSize();
	}

}