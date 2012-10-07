package ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Introducer;

public class IntroducerTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final int NAME_POSITION = 1;

	private static String[] columnHeaders = {"Nom"};

	private List<Introducer> introducerList;

	public IntroducerTableModel(List<Introducer> introducerList) {
		this.introducerList = introducerList;
	}

	@Override
	public int getRowCount() {
		return introducerList.size();
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
		if (c == NAME_POSITION) {
			clazz = String.class;
		}
		else {
			clazz = String.class;
		}
		return clazz;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Introducer introducer = introducerList.get(rowIndex);
		if (columnIndex == 0) {
			return introducer.getName();
		}
		return null;
	}
	
	public Introducer getIntroducerAt(int rank) {
		return introducerList.get(rank);
	}

}
