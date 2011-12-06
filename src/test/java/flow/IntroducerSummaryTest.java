package flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import model.GlobalSummary;
import model.Introducer;
import model.IntroducerSummary;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.HibernateUtil;

public class IntroducerSummaryTest {

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
	public void testListIntroducerSummary() {
		List<IntroducerSummary> introSummaryList = HibernateUtil
				.listReuseSession(session, IntroducerSummary.class);
		assertNotNull("IntroducerSummarySummary list not received",
				introSummaryList);
	}

	@Test
	public void testSaveIntroducerSummary() {
		List<IntroducerSummary> introSummaryList = HibernateUtil
				.listReuseSession(session, IntroducerSummary.class);
		assertNotNull("IntroducerSummary list not received", introSummaryList);

		// Create a new object
		IntroducerSummary summary = new IntroducerSummary();

		summary.setConsultationsNbr(40);

		Introducer introducer = new Introducer();
		introducer.setName("Tester test");
		HibernateUtil.saveReuseSession(session, introducer);

		summary.setIntroducer(introducer);

		HibernateUtil.saveReuseSession(session, summary);
		List<IntroducerSummary> introSummaryListAfter = HibernateUtil
				.listReuseSession(session, IntroducerSummary.class);
		assertNotNull("IntroducerSummary list not received",
				introSummaryListAfter);
		assertTrue("IntroducerSummary should contain the new summary",
				introSummaryListAfter.size() > 0);

	}

	@Test
	public void testScenario1() {
		List<GlobalSummary> globalSummaryList = HibernateUtil.listReuseSession(
				session, GlobalSummary.class);
		assertNotNull("globalSummaryList list not received", globalSummaryList);

		List<Introducer> introList = HibernateUtil.listReuseSession(session,
				Introducer.class);
		assertNotNull("introList list not received", introList);

		IntroducerSummary introSummary = new IntroducerSummary(
				introList.get(0), 1);
		HibernateUtil.saveReuseSession(session, introSummary);
	}

}
