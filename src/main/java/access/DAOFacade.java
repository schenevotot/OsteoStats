package access;

import java.util.Date;
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

	public void deleteIntroducer(Introducer introducer) {
		HibernateUtil.deleteReuseSession(session, introducer);
	}

	public void deleteGlobalSummary(GlobalSummary summary) {
		HibernateUtil.deleteReuseSession(session, summary);
	}

	public GlobalSummary saveOrUpdateGlobalSummary(GlobalSummary summary) {
		List<IntroducerSummary> introSumList = summary.getIntroducerSummaryList();
		if (introSumList != null) {
			HibernateUtil.saveOrUpdateReuseSession(session, introSumList);
		}

		HibernateUtil.saveOrUpdateReuseSession(session, summary);
		return summary;
	}

	public Introducer saveOrUpdateIntroducer(Introducer introducer) {
		HibernateUtil.saveOrUpdateReuseSession(session, introducer);
		return introducer;
	}

	public List<GlobalSummary> listAllGlobalSummary() {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSessionOrderBy(session, GlobalSummary.class,
				"week", "startDate");
		return globalSummaryList;
	}

	public List<GlobalSummary> listGlobalSummaryInRange(Date lower, Date upper, Boolean businessWeekOnly, Boolean holidaysOnly) {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSessionDateRangeOrderBy(session,
				GlobalSummary.class, "week", "startDate", lower, upper, businessWeekOnly, holidaysOnly);
		return globalSummaryList;
	}

	public List<GlobalSummary> listGlobalSummaryInRangeOrderbyNbrConsultation(Date lower, Date upper, Integer max,
			Boolean businessWeekOnly) {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSessionDateRangeNMaxOrderBy(session,
				GlobalSummary.class, "totalNbrConsultation", max, lower, upper, businessWeekOnly);
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
