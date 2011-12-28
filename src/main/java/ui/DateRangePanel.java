package ui;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

public class DateRangePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JDateChooser startDateChooser;
	private JDateChooser endDateChooser;

	public DateRangePanel() {

		JLabel fromLabel = new JLabel("Du");
		add(fromLabel);

		BugFixJTextFieldDateEditor bugFixEditor = new BugFixJTextFieldDateEditor();
		startDateChooser = new JDateChooser(bugFixEditor);
		startDateChooser.getJCalendar().setNullDateButtonVisible(true);
		add(startDateChooser);

		JLabel toLabel = new JLabel("au");
		add(toLabel);

		BugFixJTextFieldDateEditor bugFixEditor2 = new BugFixJTextFieldDateEditor();
		endDateChooser = new JDateChooser(bugFixEditor2);
		endDateChooser.getJCalendar().setNullDateButtonVisible(true);
		add(endDateChooser);
	}

	public Date getStartDate() {
		return startDateChooser.getDate();
	}

	public Date getEndDate() {
		return endDateChooser.getDate();
	}

	public DateRangeNullable getDateRange() {
		return new DateRangeNullable(getStartDate(), getEndDate());
	}

}
