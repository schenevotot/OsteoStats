package ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.GlobalSummary;
import model.ModelUtil;

public class SummaryTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final int START_DATE_POSITION = 1;

	private static final int END_DATE_POSITION = 2;

	private static String[] columnHeaders = { "Semaine", "Du", "Au",
			"Consultations", "Nouveaux patients"};

	private List<GlobalSummary> summaryList;

	public SummaryTableModel(List<GlobalSummary> summaryList) {
		this.summaryList = summaryList;
	}

	@Override
	public int getRowCount() {
		return summaryList.size();
	}

	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnHeaders[col];
	}

	/**
	 * Given a row and column index, indicates if a table cell can be edited.
	 * 
	 * @param row
	 *            Specified row index.
	 * @param column
	 *            Specified column index.
	 * @return A boolean indicating if a cell is editable.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int c) {
		Class<?> clazz = null;
		switch (c) {
		case START_DATE_POSITION:
			clazz = java.util.Date.class;
			break;
		case END_DATE_POSITION:
			clazz = java.util.Date.class;
			break;
		default:
			clazz = String.class;
			break;
		}
		return clazz;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		GlobalSummary summary = summaryList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return summary.getWeek().getWeekNbrInYear();
		case 1:
			return summary.getWeek().getStartDate();
		case 2:
			return summary.getWeek().getEndDate();
		case 3:
			return summary.getTotalNbrConsultation();
		case 4:
			return ModelUtil.processTotalNewConsultations(summary);
		default:
			break;
		}
		return null;
	}
	
	public GlobalSummary getGlobalSummaryAt(int rank) {
		return summaryList.get(rank);
	}

}
