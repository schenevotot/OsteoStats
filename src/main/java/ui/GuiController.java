package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;

import model.GlobalSummary;
import model.Introducer;
import model.Week;
import ui.stats.IntroducerSorter;
import access.DAOFacade;

public class GuiController {

	private DAOFacade daoFacade;

	public GuiController() {
		daoFacade = new DAOFacade();
	}

	public void shutdown() {
		daoFacade.shutDown();
	}

	public SummaryTableModel listAllSummary() {
		List<GlobalSummary> globalSummaryList = daoFacade.listAllGlobalSummary();
		SummaryTableModel model = new SummaryTableModel(globalSummaryList);

		return model;
	}

	public List<GlobalSummary> listNMaxSummaryInRange(DateRangeNullable dateRange, Integer max, Boolean businessWeekOnly) {
		List<GlobalSummary> globalSummaryList = daoFacade.listGlobalSummaryInRangeOrderbyNbrConsultation(
				dateRange.getLowerDate(), dateRange.getUpperDate(), max, businessWeekOnly);
		return globalSummaryList;
	}

	public List<GlobalSummary> listAllSummaryInRange(DateRangeNullable dateRange, Boolean businessWeekOnly,
			Boolean holidaysOnly) {
		List<GlobalSummary> globalSummaryList = daoFacade.listGlobalSummaryInRange(dateRange.getLowerDate(),
				dateRange.getUpperDate(), businessWeekOnly, holidaysOnly);
		return globalSummaryList;
	}

	public DefaultComboBoxModel<Introducer> listAllIntroducersAsComboBoxModel() {
		List<Introducer> introList = listAllIntroducerByImportance();
		DefaultComboBoxModel<Introducer> model = new DefaultComboBoxModel<Introducer>(
				introList.toArray(new Introducer[introList.size()]));
		return model;
	}

	private List<Introducer> listAllIntroducerByImportance() {
		// List<Introducer> introList = daoFacade.listAllIntroducer();
		List<GlobalSummary> globalSummaryList = daoFacade.listAllGlobalSummary();
		IntroducerSorter introSorter = new IntroducerSorter();
		List<Entry<Introducer, Integer>> sortedIntroducers = introSorter.sortResults(globalSummaryList);

		List<Introducer> introducers = new ArrayList<Introducer>(sortedIntroducers.size());
		for (Entry<Introducer,Integer> entry : sortedIntroducers) {
			introducers.add(entry.getKey());
		}
		return introducers;
		
	}

	public IntroducerTableModel listAllIntroducersAsTableModel() {
		List<Introducer> introList = listAllIntroducerByImportance();
		IntroducerTableModel model = new IntroducerTableModel(introList);
		return model;
	}

	public boolean containsSummaryForWeek(Week week) {
		return daoFacade.isGlobalSummaryByStartDate(week);
	}

	public GlobalSummary saveOrUpdateGlobalSummary(GlobalSummary summary) {
		return daoFacade.saveOrUpdateGlobalSummary(summary);
	}

	public Introducer saveOrUpdateIntroducer(Introducer introducer) {
		return daoFacade.saveOrUpdateIntroducer(introducer);
	}

	public void deleteGlobalSummary(GlobalSummary selectedSummary) {
		daoFacade.deleteGlobalSummary(selectedSummary);
	}

	public void deleteIntroducer(Introducer introducer) {
		daoFacade.deleteIntroducer(introducer);
	}
}
