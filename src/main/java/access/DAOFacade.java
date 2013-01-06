package access;

import java.util.Date;
import java.util.List;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;
import model.InvalidDataException;
import model.Week;

import org.hibernate.Session;

import util.HibernateUtil;

public class DAOFacade {

	private final transient Session session;

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
		return HibernateUtil.listReuseSessionOrderBy(session, GlobalSummary.class,
				"week", "startDate");
	}

	public List<GlobalSummary> listGlobalSummaryInRange(Date lower, Date upper, Boolean businessWeekOnly, Boolean holidaysOnly) {
		return HibernateUtil.listReuseSessionDateRangeOrderBy(session,
				GlobalSummary.class, "week", "startDate", lower, upper, businessWeekOnly, holidaysOnly);
	}

	public List<GlobalSummary> listGlobalSummaryInRangeOrderbyNbrConsultation(Date lower, Date upper, Integer max,
			Boolean businessWeekOnly) {
		return HibernateUtil.listReuseSessionDateRangeNMaxOrderBy(session,
				GlobalSummary.class, "totalNbrConsultation", max, lower, upper, businessWeekOnly);
	}

	public List<Introducer> listAllIntroducer() {
		return HibernateUtil.listReuseSession(session, Introducer.class);
	}

	public void shutDown() {
		HibernateUtil.closeSession(session);
		HibernateUtil.shutdown();
	}
	
	public boolean isGlobalSummaryByStartDate(Week week) {
		return HibernateUtil.findByField(session, Week.class, "startDate", week.getStartDate()) != null;
	}

}
