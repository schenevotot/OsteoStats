package flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import model.Introducer;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.HibernateUtil;

public class IntroducerTest {

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
	public void testSaveIntroducer() {
		Introducer intro = new Introducer();
		intro.setName("Tester introducer");
		HibernateUtil.saveReuseSession(session, intro);

		List<Introducer> introList = HibernateUtil.listReuseSession(session, Introducer.class);
		assertNotNull("Introducer list not received", introList);

		boolean introFound = false;
		for (Introducer intro2 : introList) {
			assertNotNull("intro2 is null", intro2);
			if (intro.getId().equals(intro2.getId())) {
				introFound = true;
			}
		}
		assertTrue("The introducer inserted has not been found again",
				introFound);

	}

	@Test
	public void testListIntroducer() {
		List<Introducer> introList = HibernateUtil.listReuseSession(session, Introducer.class);
		assertNotNull("introducer list not received", introList);
	}

}
