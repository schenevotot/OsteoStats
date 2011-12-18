package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IntroducerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Caller caller;
	private GuiController controller;
	private JPanel mainPanel;

	private IntroducerTableModel introducerTableModel;
	private JTable jTable;

	private JButton addButton;

	private JButton deleteButton;

	private JButton backButton;

	public IntroducerPanel(GuiController controller) {
		this.controller = controller;
		mainPanel = new JPanel(new BorderLayout());

		addElements();
		initElements();
		add(mainPanel);

	}

	private void addElements() {
		JPanel tablePanel = new JPanel();
		mainPanel.add(tablePanel, BorderLayout.NORTH);
		addTable(tablePanel);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		JPanel introducerButtonPanel = new JPanel();
		buttonPanel.add(introducerButtonPanel, BorderLayout.NORTH);

		addButton = new JButton("Ajouter adressant");
		introducerButtonPanel.add(addButton);

		deleteButton = new JButton("Supprimer adressant");
		introducerButtonPanel.add(deleteButton);

		JPanel goBackPanel = new JPanel();
		buttonPanel.add(goBackPanel, BorderLayout.SOUTH);

		backButton = new JButton("Retour");
		goBackPanel.add(backButton);
	}

	private void initElements() {
		addButton.setEnabled(true);
		deleteButton.setEnabled(false);

	}

	private void addTable(JPanel panel) {
		jTable = new JTable();
		jTable.setPreferredScrollableViewportSize(new Dimension(640, 480));

		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setToolTipText("Récap");

		jTable.getSelectionModel().addListSelectionListener(new IntroducerListListener());

		// TODO refactor to reuse DBClick ?
		// jTable.addMouseListener(new DoubleClickEdit());

		// First we add all introducers.
		refreshTableWithAllIntroducers();

		panel.add(new JScrollPane(jTable), BorderLayout.NORTH);
	}

	private void refreshTableWithAllIntroducers() {
		refreshTable(controller.listAllIntroducersAsTableModel());
	}

	private void refreshTable(IntroducerTableModel model) {
		// If the model is null, we ignore the refresh.
		if (model != null) {
			int selectedRow = jTable.getSelectedRow();
			String selectedIntroducerName = null;
			if (selectedRow >= 0) {
				selectedIntroducerName = (String) jTable.getValueAt(selectedRow, 0);
			}

			// Refresh the data. The previous one will be garbage collected.
			this.introducerTableModel = model;
			jTable.setModel(introducerTableModel);
			// applyColumnWidth();

			// Look for the previous introducer selected
			for (int i = 0; i < jTable.getRowCount(); i++) {
				String currentRoom = (String) jTable.getValueAt(i, 0);
				if (currentRoom.equals(selectedIntroducerName)) {
					jTable.setRowSelectionInterval(i, i);
					break;
				}
			}
		}
	}

	public void setCaller(Caller caller) {
		this.caller = caller;

	}

	private class IntroducerListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int selectedRow = jTable.getSelectedRow();
			// Check if a row is selected in the table
			if (selectedRow > -1) {
				deleteButton.setEnabled(true);
			} else {
				// Disable the button if no row is selected (when the table is
				// empty for example)
				deleteButton.setEnabled(false);
			}
		}
	}

}
