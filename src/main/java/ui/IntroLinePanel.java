package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Introducer;
import model.IntroducerSummary;

public class IntroLinePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<Introducer> introComboModel;
	private JTextField txtField;
	private JComboBox<Introducer> combo;

	private IntroducerSummary introSummary;
	private GuiController controller;

	public IntroLinePanel(IntroducerSummary introSummary, final Integer rank, final PatientLinePanel panel,
			GuiController controller) {
		this.introSummary = introSummary;
		this.controller = controller;
		txtField = new JTextField(2);

		combo = new JComboBox<Introducer>();
		combo.setRenderer(new IntroducerComboBoxRenderer());

		JLabel txtLabel = new JLabel("patients recommandés par");
		JButton removeLine = createRemoveLineButton(rank, panel);

		init();

		add(txtField);
		add(txtLabel);
		add(combo);
		add(removeLine);
	}

	private JButton createRemoveLineButton(final Integer rank, final PatientLinePanel panel) {
		JButton removeLine = new JButton();
		removeLine.setIcon(new ImageIcon(getClass().getResource("/icons/cross.png")));
		removeLine.setBorderPainted(false);
		removeLine.setContentAreaFilled(false);
		removeLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeLine(rank);
			}
		});
		return removeLine;
	}

	public DefaultComboBoxModel<Introducer> getIntroComboModel() {
		return introComboModel;
	}

	public JTextField getTxtField() {
		return txtField;
	}

	public JComboBox<Introducer> getCombo() {
		return combo;
	}

	private void init() {
		refreshIntroducerList();
		if (introSummary != null) {
			txtField.setText(String.valueOf(introSummary.getConsultationsNbr()));
			Introducer intro = introSummary.getIntroducer();
			combo.setSelectedItem(intro);
		} else {
			txtField.setText(String.valueOf(0));
		}
	}

	public final void refreshIntroducerList() {
		//Save the selection
		Object selection = combo.getSelectedItem();
		introComboModel = controller.listAllIntroducersAsComboBoxModel();
		combo.setModel(introComboModel);
		if (selection != null) {
			combo.setSelectedItem(selection);
		}
	}
	
	public void setDropDownEnabled(boolean enable) {
		combo.setEnabled(enable);
	}
	
	public IntroducerSummary getIntroSummary() {
		return introSummary;
	}

}
