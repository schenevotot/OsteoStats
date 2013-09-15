package ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;

public class PatientLinePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;
	private SummaryDialog summaryDialog;
	private GuiController controller;
	private Map<Integer, IntroLinePanel> introLinePanelMap = new HashMap<Integer, IntroLinePanel>();

	private Integer introUIId;

	private JTextField totalTextField;

	private JTextField saturdayTextField;

	private GlobalSummary summary;

	public PatientLinePanel(MainWindow mainWindow, SummaryDialog summaryDialog, GlobalSummary summary,
			GuiController controller) {
		super(new FlowLayout());
		this.controller = controller;
		this.summary = summary;
		this.mainWindow = mainWindow;
		this.summaryDialog = summaryDialog;
		this.introUIId = 0;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Border blackline = BorderFactory.createLineBorder(Color.black);
		setBorder(blackline);

		addTotalLine();
		// Add the tech line (Editer adressants, +) in the same panel
		addTechLine();
		
		init(summary);
	}

	private void init(GlobalSummary summary) {
		if (summary == null) {
			newLine();
			saturdayTextField.setText(String.valueOf(0));
			totalTextField.setText(String.valueOf(0));
		} else {
			List<IntroducerSummary> introSummaryList = summary.getIntroducerSummaryList();
			if (introSummaryList != null) {
				for (IntroducerSummary introducerSummary : introSummaryList) {
					newLine(introducerSummary);
				}
			}
			saturdayTextField.setText(String.valueOf(summary.getNbrConsultationSaturday()));
			totalTextField.setText(String.valueOf(summary.getTotalNbrConsultation()));
		}
	}

	public final synchronized void newLine(IntroducerSummary introSummary) {
		IntroLinePanel introLinePanel = new IntroLinePanel(introSummary, introUIId, this, controller);
		// Here we set the size +1 (because of the "Total" line, and not the id
		add(introLinePanel, introLinePanelMap.size() + 1);
		introLinePanelMap.put(introUIId, introLinePanel);
		introUIId++;
	}

	public final synchronized void newLine() {
		newLine(null);
	}

	public final void refreshAllIntroducers() {
		for (IntroLinePanel introPanel : introLinePanelMap.values()) {
			introPanel.refreshIntroducerList();
		}
	}

	private void addTechLine() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		JButton editButton = new JButton("Éditer adressants", new ImageIcon(getClass().getResource(
				"/icons/application_form_edit.png")));
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				summaryDialog.setVisible(false);
				mainWindow.navigateToIntroducerPane(summaryDialog);

			}
		});

		panel.add(editButton);
		JButton newLineButton = createAddLineButton();
		panel.add(newLineButton);
		add(panel);
	}

	public synchronized void removeLine(Integer id) {
		// Here, we don't touch the Id
		IntroLinePanel panelToRemove = introLinePanelMap.get(id);
		introLinePanelMap.remove(id);
		remove(panelToRemove);
		invalidate();
		revalidate();
	}

	private final JButton createAddLineButton() {
		JButton newLine = new JButton();
		newLine.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
		newLine.setBorderPainted(false);
		newLine.setContentAreaFilled(false);
		newLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newLine();
			}
		});
		return newLine;
	}

	private void addTotalLine() {
		JPanel totalPanel = new JPanel();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		totalPanel.setBorder(blackline);
		JLabel totalLabel = new JLabel("Total:");

		totalTextField = new JTextField(2);

		JLabel patientLabel = new JLabel("patients dont");

		saturdayTextField = new JTextField(2);

		JLabel saturdayLabel = new JLabel("le samedi");
		totalPanel.add(totalLabel);
		totalPanel.add(totalTextField);
		totalPanel.add(patientLabel);
		totalPanel.add(saturdayTextField);
		totalPanel.add(saturdayLabel);
		add(totalPanel);
	}

	public GlobalSummary createPartialSummaryFromUI() {
		GlobalSummary summaryFromUI = null;
		if (summary != null) {
			summaryFromUI = summary;
		} else {
			summaryFromUI = new GlobalSummary();
		}

		summaryFromUI.setNbrConsultationSaturday(Integer.valueOf(saturdayTextField.getText()));
		summaryFromUI.setTotalNbrConsultation(Integer.valueOf(totalTextField.getText()));

		// Removing all previous IntroSummary
		if (summaryFromUI.getIntroducerSummaryList() != null) {
			summaryFromUI.getIntroducerSummaryList().clear();
		}

		Collection<IntroLinePanel> introLineCollection = introLinePanelMap.values();
		for (IntroLinePanel introLinePanel : introLineCollection) {
			Integer consultNbr = Integer.valueOf(introLinePanel.getTxtField().getText());
			IntroducerSummary introSummary = introLinePanel.getIntroSummary();

			if (introSummary == null) {
				introSummary = new IntroducerSummary();
			}
			summaryFromUI.addIntroducerSummary(introSummary);
			introSummary.setIntroducer(((Introducer) (introLinePanel.getIntroComboModel().getSelectedItem())));

			if (consultNbr > 0) {
				introSummary.setConsultationsNbr(consultNbr);
			}

		}

		return summaryFromUI;
	}
	
	public void setAllDropDownEnabled(boolean enable) {
		Collection<IntroLinePanel> introLineCollection = introLinePanelMap.values();
		for (IntroLinePanel introLinePanel : introLineCollection) {
			introLinePanel.setDropDownEnabled(enable);
		}
	}

}
