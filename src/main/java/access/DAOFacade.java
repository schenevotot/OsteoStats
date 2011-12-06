package access;

import java.util.List;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;

import org.hibernate.Session;

import util.HibernateUtil;

public class DAOFacade {

	private Session session;
	
	public DAOFacade() {
		super();
		session = HibernateUtil.createSession();
		
	}

	/*public Week saveNewWeek(Integer weekNbrInYear, Date startDate,
			Date endDate, Boolean businessWeek, Boolean schoolHolidaysWeek) {
		Week week = new Week(weekNbrInYear, startDate, endDate, businessWeek,
				schoolHolidaysWeek);
		HibernateUtil.save(week);
		return week;
	}*/

	/*public Introducer saveNewIntroducer(String name) {
		Introducer intro = new Introducer(name);
		HibernateUtil.save(intro);
		return intro;
	}*/

	/*public IntroducerSummary saveNewIntroducerSummary(Introducer introducer,
			Integer consultationsNbr) {
		IntroducerSummary introSummary = new IntroducerSummary(introducer,
				consultationsNbr);
		HibernateUtil.save(introSummary);
		return introSummary;
	}*/

	/*public GlobalSummary saveNewGlobalSummary(Week week,
			List<IntroducerSummary> introducerSummaryList,
			Integer nbrConsultationSaturday, Integer totalNbrConsultation) {
		GlobalSummary summary = new GlobalSummary(week, introducerSummaryList,
				nbrConsultationSaturday, totalNbrConsultation);
		HibernateUtil.save(summary);
		return summary;
	}*/
	
	public void deleteGlobalSummary(GlobalSummary summary) {
		HibernateUtil.deleteReuseSession(session, summary);
	}
	
	public GlobalSummary saveOrUpdateGlobalSummary(GlobalSummary summary) {
		List<IntroducerSummary> introSumList = summary.getIntroducerSummaryList();
		if (introSumList != null) {
			for (IntroducerSummary introducerSummary : introSumList) {
				if (introducerSummary.getId() == null) {
					HibernateUtil.saveOrUpdateReuseSession(session, introducerSummary);
				}
				if (introducerSummary.getId() == null) {
					System.err.println("IntroducerSummary added but still no Id");
				}
			}
		}
		
		HibernateUtil.saveReuseSession(session, summary);
		return summary;
	}
	
	public List<GlobalSummary> listAllGlobalSummary() {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSessionOrderBy(session, GlobalSummary.class, "week", "startDate");
		return globalSummaryList;
	}
	
	public List<Introducer> listAllIntroducer() {
		List<Introducer> introducerList = HibernateUtil.listReuseSession(session, Introducer.class);
		return introducerList;
	}

	public void shutDown() {
		HibernateUtil.closeSession(session);
		HibernateUtil.shutdown();
	}

}
