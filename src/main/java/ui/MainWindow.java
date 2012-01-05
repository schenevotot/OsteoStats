package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ui.stats.StatsManager;
import ui.stats.TopTenMonth;
import ui.stats.TopTenWeek;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private GuiController controller;

	private JTabbedPane tabbedPane;

	private SummaryPanel summaryPanel;

	private IntroducerPanel introducerPanel;

	private JPanel statPanel;

	public MainWindow() {
		super("Osteo stats");
		controller = new GuiController();
		Runtime.getRuntime().addShutdownHook(new Shutdown(controller));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());
		add(mainPanel);
		addTabbedPane();

		pack();
		// Put it in the middle of the screen
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);
	}

	private void addTabbedPane() {
		tabbedPane = new JTabbedPane();

		summaryPanel = new SummaryPanel(this, controller, new BorderLayout());
		tabbedPane.add("Récapitulatif", summaryPanel);

		introducerPanel = new IntroducerPanel(controller);
		tabbedPane.add("Adressants", introducerPanel);
		
		statPanel = buildStatPanel();
		tabbedPane.add("Statistiques", statPanel);
		
		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		mainPanel.add(tabbedPane);
	}
	
	
	private JPanel buildStatPanel() {
		StatsManager manager = new StatsManager();
		manager.plugStat(new TopTenWeek(controller));
		manager.plugStat(new TopTenMonth(controller));
		
		return manager.processStatsSummaryPanel();
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public SummaryPanel getSummaryPanel() {
		return summaryPanel;
	}

	public static void main(String[] args) {
		// Set the default locale to french
		Locale.setDefault(Locale.FRANCE);
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	protected static void createAndShowGUI() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		new MainWindow();

	}

	public void navigateToIntroducerPane(Caller summaryDialog) {
		introducerPanel.setCaller(summaryDialog);
		getTabbedPane().setSelectedComponent(introducerPanel);
		
	}

}
