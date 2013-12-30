package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.GlobalSummary;

public class SummaryPanel extends JPanel {

	private static final Border CHISEL_BORDER = new ChiselBorder();
	private static final long serialVersionUID = 1L;
	private JTable jTable;
	private SummaryTableModel summaryTableModel;
	private GuiController controller;
	private MainWindow mainWindow;

	private JButton editWeekButton;
	private JButton addWeekButton;
	private JButton removeWeekButton;

	public SummaryPanel(MainWindow mainWindow, GuiController controller, LayoutManager layout) {
		super(layout);
		this.controller = controller;
		this.mainWindow = mainWindow;

		setPreferredSize(new Dimension(800, 600));
		addTable();
		addButtonsPanel();
		setBorder(CHISEL_BORDER);
	}

	private void addTable() {
		jTable = new JTable();
		jTable.setPreferredScrollableViewportSize(new Dimension(640, 480));

		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setToolTipText("Récap");

		jTable.getSelectionModel().addListSelectionListener(new SummaryListListener());

		jTable.addMouseListener(new DoubleClickEdit());

		// First we add all summaries.
		refreshTableWithAllSummaries();

		add(new JScrollPane(jTable), BorderLayout.NORTH);
	}

	private void addButtonsPanel() {
		JPanel buttonPanel = new JPanel();
		addWeekButton = new JButton("Ajouter semaine");
		addWeekButton.addActionListener(new NewOrModifySummaryListener(mainWindow, false));
		buttonPanel.add(addWeekButton);

		editWeekButton = new JButton("Modifier semaine");
		editWeekButton.setEnabled(false);
		editWeekButton.addActionListener(new NewOrModifySummaryListener(mainWindow, true));
		buttonPanel.add(editWeekButton);

		removeWeekButton = new JButton("Supprimer semaine");
		removeWeekButton.addActionListener(new DeleteSummaryListener(this));
		buttonPanel.add(removeWeekButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void refreshTable(SummaryTableModel model) {
		// If the model is null, we ignore the refresh.
		if (model != null) {
			int selectedRow = jTable.getSelectedRow();
			Integer selectedRecordID = null;
			if (selectedRow >= 0) {
				selectedRecordID = (Integer) jTable.getValueAt(selectedRow, 0);
			}

			// Refresh the data. The previous one will be garbage collected.
			this.summaryTableModel = model;
			jTable.setModel(summaryTableModel);
			// applyColumnWidth();

			// Look for the previous summary selected
			for (int i = 0; i < jTable.getRowCount(); i++) {
				Integer currentRoom = (Integer) jTable.getValueAt(i, 0);
				if (currentRoom.equals(selectedRecordID)) {
					jTable.setRowSelectionInterval(i, i);
					break;
				}
			}
		}
	}

	private void setAllButtonsEnabled(boolean enabled) {
		editWeekButton.setEnabled(enabled);
		addWeekButton.setEnabled(enabled);
		removeWeekButton.setEnabled(enabled);
	}

	public void disableAllButtons() {
		setAllButtonsEnabled(false);
	}

	public void enableAllButtons() {
		setAllButtonsEnabled(true);
	}

	public final void refreshTableWithAllSummaries() {
		refreshTable(controller.listAllSummary());
	}

	private class NewOrModifySummaryListener implements ActionListener {

		private MainWindow mainWindow;
		private boolean modifySummary;

		public NewOrModifySummaryListener(MainWindow mainWindow, boolean modifySummary) {
			this.mainWindow = mainWindow;
			this.modifySummary = modifySummary;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = jTable.getSelectedRow();
			SummaryDialog dialog = null;
			// Check if a row is selected in the table
			if (modifySummary && selectedRow > -1) {
				GlobalSummary selectedSummary = summaryTableModel.getGlobalSummaryAt(selectedRow);
				dialog = new SummaryDialog(mainWindow, controller, selectedSummary);

			} else {
				dialog = new SummaryDialog(mainWindow, controller);
			}
			mainWindow.getSummaryPanel().disableAllButtons();
			dialog.display();

		}
	}

	private class DeleteSummaryListener implements ActionListener {

		private JPanel panel;

		public DeleteSummaryListener(JPanel panel) {
			this.panel = panel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = jTable.getSelectedRow();
			if (selectedRow > -1) {
				GlobalSummary selectedSummary = summaryTableModel.getGlobalSummaryAt(selectedRow);

				int n = JOptionPane.showConfirmDialog(panel, "Etes-vous sûr de vouloir le supprimer ?",
						"Etes-vous sûr ?", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					controller.deleteGlobalSummary(selectedSummary);
					refreshTableWithAllSummaries();
				}
			}
		}
	}

	public class DoubleClickEdit extends MouseAdapter {

		public DoubleClickEdit() {

		}

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int selectedRow = target.getSelectedRow();

				if (selectedRow > -1) {
					GlobalSummary selectedSummary = summaryTableModel.getGlobalSummaryAt(selectedRow);
					SummaryDialog dialog = new SummaryDialog(mainWindow, controller, selectedSummary);
					dialog.display();
				}
			}
		}
	}

	private static class ChiselBorder implements Border {
		private Insets insets = new Insets(1, 0, 1, 0);

		public ChiselBorder() {
		}

		public Insets getBorderInsets(Component c) {
			return insets;
		}

		public boolean isBorderOpaque() {
			return true;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Color color = c.getBackground();
			// render highlight at top
			g.setColor(deriveColorHSB(color, 0, 0, .2f));
			g.drawLine(x, y, x + width, y);
			// render shadow on bottom
			g.setColor(deriveColorHSB(color, 0, 0, -.2f));
			g.drawLine(x, y + height - 1, x + width, y + height - 1);
		}
	}

	/**
	 * Derives a color by adding the specified offsets to the base color's hue,
	 * saturation, and brightness values. The resulting hue, saturation, and
	 * brightness values will be contrained to be between 0 and 1.
	 * 
	 * @param base
	 *            the color to which the HSV offsets will be added
	 * @param dH
	 *            the offset for hue
	 * @param dS
	 *            the offset for saturation
	 * @param dB
	 *            the offset for brightness
	 * @return Color with modified HSV values
	 */
	public static Color deriveColorHSB(Color base, float dH, float dS, float dB) {
		float hsb[] = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);

		hsb[0] += dH;
		hsb[1] += dS;
		hsb[2] += dB;
		return Color.getHSBColor(hsb[0] < 0 ? 0 : (hsb[0] > 1 ? 1 : hsb[0]),
				hsb[1] < 0 ? 0 : (hsb[1] > 1 ? 1 : hsb[1]), hsb[2] < 0 ? 0 : (hsb[2] > 1 ? 1 : hsb[2]));

	}

	private class SummaryListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int selectedRow = jTable.getSelectedRow();
			// Check if a row is selected in the table
			if (selectedRow > -1) {
				// GlobalSummary summary = summaryTableModel
				// .getGlobalSummaryAt(selectedRow);
				editWeekButton.setEnabled(true);
			} else {
				// Disable the button if no row is selected (when the table is
				// empty for example)
				editWeekButton.setEnabled(false);
			}
		}
	}
}
