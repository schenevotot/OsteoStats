package flow;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;
import model.Week;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.HibernateUtil;

public class GlobalSummaryTest {

	Session session;

	@Before
	public void setUp() throws Exception {
		session = HibernateUtil.createSession();
	}

	@After
	public void tearDown() throws Exception {
		HibernateUtil.closeSession(session);
	}

	@Test
	public void testListGlobalSummary() {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSession(
				session, GlobalSummary.class);
		assertNotNull("GlobalSummary list not received", globalSummaryList);
	}

	@Test
	public void testSaveGlobalSummary() {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSession(
				session, GlobalSummary.class);
		assertNotNull("ConsultationsSummary list not received",
				globalSummaryList);

		// Create a new object
		GlobalSummary summary = new GlobalSummary();

		// Create a week
		Week week = createWeek();

		// Associate the week
		summary.setWeek(week);

		summary.setNbrConsultationSaturday(3);
		List<IntroducerSummary> summaryList = new ArrayList<IntroducerSummary>();

		IntroducerSummary introSummary1 = createIntroducerSummary();
		summaryList.add(introSummary1);

		IntroducerSummary introSummary2 = createIntroducerSummary2();
		summaryList.add(introSummary2);

		summary.setIntroducerSummaryList(summaryList);
		summary.setTotalNbrConsultation(60);

		HibernateUtil.saveReuseSession(session, summary);

	}

	private IntroducerSummary createIntroducerSummary() {
		IntroducerSummary summary = new IntroducerSummary();
		summary.setConsultationsNbr(10);

		Introducer introducer = new Introducer();
		introducer.setName("Tester test");
		HibernateUtil.saveReuseSession(session, introducer);

		summary.setIntroducer(introducer);
		HibernateUtil.saveReuseSession(session, summary);
		return summary;
	}

	private Week createWeek() {
		// Create a week
		Week week = new Week();
		week.setWeekNbrInYear(42);
		week.setSchoolHolidaysWeek(Boolean.FALSE);
		week.setBusinessWeek(100);
		HibernateUtil.saveReuseSession(session, week);
		return week;
	}

	private IntroducerSummary createIntroducerSummary2() {
		IntroducerSummary summary = new IntroducerSummary();

		summary.setConsultationsNbr(2);

		Introducer introducer = new Introducer();
		introducer.setName("Tester 2");
		HibernateUtil.saveReuseSession(session, introducer);

		summary.setIntroducer(introducer);
		HibernateUtil.saveReuseSession(session, summary);
		return summary;
	}

}
