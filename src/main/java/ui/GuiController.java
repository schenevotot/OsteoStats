package ui;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import model.GlobalSummary;
import model.Introducer;
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

	public DefaultComboBoxModel<Introducer> listAllIntroducersAsComboBoxModel() {
		List<Introducer> introList = daoFacade.listAllIntroducer();
		DefaultComboBoxModel<Introducer> model = new DefaultComboBoxModel<Introducer>(
				introList.toArray(new Introducer[introList.size()]));
		return model;
	}

	public IntroducerTableModel listAllIntroducersAsTableModel() {
		List<Introducer> introList = daoFacade.listAllIntroducer();
		IntroducerTableModel model = new IntroducerTableModel(introList);
		return model;
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
