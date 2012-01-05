package ui;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DateRangePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JDateChooser startDateChooser;
	private JDateChooser endDateChooser;
	private JTextFieldDateEditor bugFixEditor;
	private JTextFieldDateEditor bugFixEditor2;

	public DateRangePanel() {

		JLabel fromLabel = new JLabel("Du");
		add(fromLabel);

		bugFixEditor = new BugFixJTextFieldDateEditor();
		startDateChooser = new JDateChooser(bugFixEditor);
		startDateChooser.getJCalendar().setNullDateButtonVisible(true);
		add(startDateChooser);

		JLabel toLabel = new JLabel("au");
		add(toLabel);

		bugFixEditor2 = new BugFixJTextFieldDateEditor();
		endDateChooser = new JDateChooser(bugFixEditor2);
		endDateChooser.getJCalendar().setNullDateButtonVisible(true);
		add(endDateChooser);
	}

	public Date getStartDate() {
		return startDateChooser.getDate();
	}

	public JTextFieldDateEditor getStartDateEditor() {
		return bugFixEditor;
	}

	public void setStartDate(Date date) {
		startDateChooser.setDate(date);
	}

	public void setStartDateEditorEnabled(boolean enable) {
		startDateChooser.setEnabled(enable);
	}

	public Date getEndDate() {
		return endDateChooser.getDate();
	}

	public JTextFieldDateEditor getEndDateEditor() {
		return bugFixEditor2;
	}

	public void setEndDate(Date date) {
		endDateChooser.setDate(date);
	}

	public void setEndDateEditorEnabled(boolean enable) {
		endDateChooser.setEnabled(enable);
	}

	public DateRangeNullable getDateRange() {
		return new DateRangeNullable(getStartDate(), getEndDate());
	}

}
