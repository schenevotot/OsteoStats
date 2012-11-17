package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;
import model.Week;
import util.DateUtil;

public class SummaryDialog extends JDialog implements Caller {

	private static final long serialVersionUID = 1L;
	private static final String DIALOG_TITLE_NEW = "Nouvelle semaine";
	private static final String DIALOG_TITLE_OLD = "Modifier semaine";

	private GuiController controller;

	private MainWindow mainWindow;
	private GlobalSummary summary;
	private JPanel mainPanel;

	private JTextField weekNbrField;

	private JPanel textPanelPart2;
	private JTextField businessWeek;
	private JCheckBox schoolHolidayWeek;
	private PatientLinePanel patientLinePanel;
	private DateRangePanel dateRangePanel;
	private boolean isModify;

	public SummaryDialog(MainWindow mainWindow, GuiController controller) {
		this(mainWindow, controller, null);
	}

	public SummaryDialog(MainWindow mainWindow, GuiController controller, GlobalSummary summary) {
		super();
		this.controller = controller;
		this.summary = summary;
		this.mainWindow = mainWindow;

		setResizable(true);

		if (summary == null) {
			this.isModify = false;
			setTitle(DIALOG_TITLE_NEW);
		} else {
			this.isModify = true;
			setTitle(DIALOG_TITLE_OLD);
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());

		addElements();
		initElements();
		setPreferredSize(new Dimension(512, 384));
		add(new JScrollPane(mainPanel));
	}

	private void addElements() {
		addPart1(mainPanel, BorderLayout.NORTH);
		addPart2(mainPanel, BorderLayout.CENTER);
		addPart3(mainPanel, BorderLayout.SOUTH);
	}

	private void addPart1(JPanel textPanel, String cstraint) {
		JPanel textPanelPart1 = new JPanel(new GridLayout(2, 1));
		textPanel.add(textPanelPart1, cstraint);
		addPart1Line1(textPanelPart1);
		addPart1Line2(textPanelPart1);
	}

	private void addPart1Line1(JPanel textPanelPart1) {
		JPanel textPanelPart1Line1 = new JPanel();
		textPanelPart1.add(textPanelPart1Line1);

		dateRangePanel = new DateRangePanel();
		dateRangePanel.getStartDateEditor().addFocusListener(new DateChangeFocusListener());
		dateRangePanel.getEndDateEditor().addFocusListener(new DateChangeFocusListener());
		textPanelPart1Line1.add(dateRangePanel);

		weekNbrField = new JTextField(2);
		weekNbrField.setEnabled(false);

		textPanelPart1Line1.add(weekNbrField);
	}

	private void addPart1Line2(JPanel textPanelPart1) {
		JPanel textPanelPart1Line2 = new JPanel();
		textPanelPart1.add(textPanelPart1Line2);
		businessWeek = new JTextField("Proportion travaillée (%)");
		textPanelPart1Line2.add(businessWeek);
		JLabel businessWeekLabel = new JLabel("% travaillée");
		textPanelPart1Line2.add(businessWeekLabel);

		schoolHolidayWeek = new JCheckBox("Vacances scolaires");
		textPanelPart1Line2.add(schoolHolidayWeek);
	}

	private void addPart2(JPanel textPanel, String cstraint) {
		textPanelPart2 = new JPanel();
		textPanel.add(textPanelPart2, cstraint);
		patientLinePanel = new PatientLinePanel(mainWindow, this, summary, controller);
		textPanelPart2.add(patientLinePanel);
	}

	private final void addPart3(JPanel textPanel, String cstraint) {
		JPanel textPanelPart3 = new JPanel();
		textPanel.add(textPanelPart3, cstraint);

		JButton cancelButton = new JButton("Annuler");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		textPanelPart3.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ValidateSummaryActionListener());
		textPanelPart3.add(okButton);

	}

	public void display() {
		pack();
		setLocationRelativeTo(mainWindow.getSummaryPanel());
		setVisible(true);
	}

	private class DateChangeFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent event) {
			if (event.getComponent() == dateRangePanel.getStartDateEditor() && dateRangePanel.getStartDate() != null) {
				Date startDate = dateRangePanel.getStartDate();
				Date endDate = DateUtil.createEndDateFromStartDate(startDate);
				dateRangePanel.setEndDate(endDate);
				int weekNbr = DateUtil.getWeekNbr(startDate);
				updateWeekNbr(weekNbr);
			} else if (event.getComponent() == dateRangePanel.getEndDateEditor() && dateRangePanel.getEndDate() != null) {
				Date endDate = dateRangePanel.getEndDate();
				Date startDate = DateUtil.createStartDateFromEndDate(endDate);
				dateRangePanel.setStartDate(startDate);
				int weekNbr = DateUtil.getWeekNbr(startDate);
				updateWeekNbr(weekNbr);
			}
		}
	}

	private void updateWeekNbr(int weekNbr) {
		weekNbrField.setText(String.valueOf(weekNbr));
	}

	private void initElements() {
		if (summary != null) {
			Week week = summary.getWeek();
			updateWeekNbr(week.getWeekNbrInYear());
			dateRangePanel.setStartDate(week.getStartDate());
			dateRangePanel.setEndDate(week.getEndDate());
			businessWeek.setText(String.valueOf(week.getBusinessWeek()));
			schoolHolidayWeek.setSelected(week.getSchoolHolidaysWeek());

			// Desactivate most options
			dateRangePanel.setStartDateEditorEnabled(false);
			dateRangePanel.setEndDateEditorEnabled(false);
			patientLinePanel.setAllDropDownEnabled(false);

		} else {
			// Set some default values
			Date startDate = DateUtil.getStartDateSuggestion();
			updateWeekNbr(DateUtil.getWeekNbr(startDate));
			dateRangePanel.setStartDate(startDate);
			dateRangePanel.setEndDate(DateUtil.getEndDateSuggestion());
			businessWeek.setText("100");
			schoolHolidayWeek.setSelected(false);
		}
	}

	private GlobalSummary createSummaryFromUI() {
		GlobalSummary summaryFromUI = patientLinePanel.createPartialSummaryFromUI();
		Week week = null;
		if (summary != null) {
			week = summary.getWeek();
		} else {
			week = new Week();
			week.setWeekNbrInYear(Integer.valueOf(weekNbrField.getText()));
			week.setStartDate(dateRangePanel.getStartDate());
			week.setEndDate(dateRangePanel.getEndDate());
		}
		week.setBusinessWeek(Integer.valueOf(businessWeek.getText()));
		week.setSchoolHolidaysWeek(schoolHolidayWeek.isSelected());

		summaryFromUI.setWeek(week);

		return summaryFromUI;
	}

	private void displayPopupWithErrors(List<UIError> errorList) {
		StringBuffer message = new StringBuffer("Merci de revoir les erreurs suivantes:\n\r");
		for (UIError uiError : errorList) {
			message.append(" - ");
			message.append(uiError.getMessage());
			message.append("\n\r");
		}

		JOptionPane.showMessageDialog(mainWindow.getSummaryPanel(), message, "Please review the following errors",
				JOptionPane.ERROR_MESSAGE);

	}

	private List<UIError> validateSummary(GlobalSummary summary) {
		List<UIError> errorList = new ArrayList<UIError>();
		if (summary.getTotalNbrConsultation() == null || summary.getTotalNbrConsultation() < 0) {
			UIError error = new UIError("Nombre total de consultations invalide");
			errorList.add(error);
		}
		if (summary.getNbrConsultationSaturday() == null || summary.getNbrConsultationSaturday() < 0) {
			UIError error = new UIError("Nombre de consultations le samedi invalide");
			errorList.add(error);
		}
		List<IntroducerSummary> introSummaryList = summary.getIntroducerSummaryList();
		if (introSummaryList != null) {
			int nbrPatientsRecommended = 0;
			Set<Introducer> introducerSet = new HashSet<Introducer>(introSummaryList.size());
			for (IntroducerSummary introducerSummary : introSummaryList) {
				Introducer intro = introducerSummary.getIntroducer();
				if (introducerSet.contains(intro)) {
					UIError error = new UIError("Plusieurs occurrences de " + intro.getName());
					errorList.add(error);
				} else {
					introducerSet.add(intro);
				}

				if (introducerSummary.getConsultationsNbr() == null || introducerSummary.getConsultationsNbr() < 0) {
					UIError error = new UIError("Nombre de patients recommandés par " + intro.getName() + " invalide");
					errorList.add(error);
				} else {
					nbrPatientsRecommended += introducerSummary.getConsultationsNbr();
				}

			}
			if (nbrPatientsRecommended > summary.getTotalNbrConsultation()) {
				UIError error = new UIError(nbrPatientsRecommended
						+ " patients ont été recommandés mais le total est de " + summary.getTotalNbrConsultation());
				errorList.add(error);
			}
			if (summary.getNbrConsultationSaturday() > summary.getTotalNbrConsultation()) {
				UIError error = new UIError("Il y a plus de patients le samedi qu'au total");
				errorList.add(error);
			}
		}
		Week week = summary.getWeek();
		if (!DateUtil.isMonday(week.getStartDate()) || !DateUtil.isMonday(week.getEndDate())) {
			UIError error = new UIError("Les dates doivent être des lundis");
			errorList.add(error);
		}
		if (!isModify && controller.containsSummaryForWeek(week)) {
			UIError error = new UIError("Il y a déjà un rapport pour cette semaine");
			errorList.add(error);
		}

		return errorList;
	}

	@Override
	public void callBack() {
		patientLinePanel.refreshAllIntroducers();
		setVisible(true);
	}

	@Override
	public void dispose() {
		super.dispose();
		mainWindow.getSummaryPanel().enableAllButtons();
	}

	private class ValidateSummaryActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			GlobalSummary summaryFromUI = createSummaryFromUI();
			List<UIError> errorList = validateSummary(summaryFromUI);
			if (!errorList.isEmpty()) {
				displayPopupWithErrors(errorList);
				removeIncoherentParameters(summaryFromUI);
			} else {
				controller.saveOrUpdateGlobalSummary(summaryFromUI);
				mainWindow.getSummaryPanel().refreshTableWithAllSummaries();
				dispose();
			}
		}

	}

	private void removeIncoherentParameters(GlobalSummary summaryFromUI) {
		List<IntroducerSummary> introSummaryList = summaryFromUI.getIntroducerSummaryList();

		if (introSummaryList != null && introSummaryList.size() > 1) {

			Set<IntroducerSummary> toRemoveSet = new HashSet<IntroducerSummary>();
			// Sort them by IntroducerName
			List<IntroducerSummary> sortedIntroSummaryList = new ArrayList<IntroducerSummary>(introSummaryList);
			Collections.sort(sortedIntroSummaryList, new Comparator<IntroducerSummary>() {

				@Override
				public int compare(IntroducerSummary o1, IntroducerSummary o2) {
					return o1.getIntroducer().compareTo(o2.getIntroducer());
				}
			});

			for (int i = 0; i < sortedIntroSummaryList.size() - 1; i++) {
				IntroducerSummary introSummary0 = sortedIntroSummaryList.get(i);
				IntroducerSummary introSummary1 = sortedIntroSummaryList.get(i + 1);
				if (introSummary0.getIntroducer().equals(introSummary1.getIntroducer())) {
					if (introSummary0.getId() == null) {
						toRemoveSet.add(introSummary0);
					} else if (introSummary1.getId() == null) {
						toRemoveSet.add(introSummary1);
					}
				}
			}

			introSummaryList.removeAll(toRemoveSet);
		}
	}
}
