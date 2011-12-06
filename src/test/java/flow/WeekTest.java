package flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import model.Week;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.HibernateUtil;

public class WeekTest {

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
	public void testSimpleSaveWeek() {

		Week week = new Week();
		week.setStartDate(new Date());
		week.setBusinessWeek(Boolean.FALSE);
		week.setEndDate(new Date());
		week.setWeekNbrInYear(5);
		HibernateUtil.saveReuseSession(session, week);
		assertNotNull("week list not received", week.getId());
	}

	@Test
	public void testSaveWeek() {

		Week week = new Week();
		week.setStartDate(new Date());
		week.setBusinessWeek(Boolean.FALSE);
		week.setEndDate(new Date());
		week.setWeekNbrInYear(5);
		HibernateUtil.saveReuseSession(session, week);

		List<Week> weekList = HibernateUtil.listReuseSession(session, Week.class);
		assertNotNull("week list not received", weekList);

		boolean weekFound = false;
		for (Week week2 : weekList) {
			assertNotNull("week is null", week2);
			if (week.getId().equals(week2.getId())) {
				weekFound = true;
			}
		}
		assertTrue("The week inserted has not been found again", weekFound);

	}

	@Test
	public void testSaveWeek2() {

		Week week = new Week();
		week.setStartDate(new Date());
		week.setBusinessWeek(Boolean.FALSE);
		week.setEndDate(new Date());
		week.setWeekNbrInYear(5);
		HibernateUtil.saveReuseSession(session, week);

		Week week3 = new Week();
		week3.setWeekNbrInYear(42);
		week3.setSchoolHolidaysWeek(Boolean.FALSE);
		week3.setBusinessWeek(Boolean.TRUE);
		HibernateUtil.saveReuseSession(session, week3);

		List<Week> weekList = HibernateUtil.listReuseSession(session, Week.class);
		assertNotNull("week list not received", weekList);

		boolean weekFound = false;
		for (Week week2 : weekList) {
			assertNotNull("week is null", week2);
			if (week.getId().equals(week2.getId())) {
				weekFound = true;
			}
		}
		assertTrue("The week inserted has not been found again", weekFound);

	}

	@Test
	public void testListWeek() {
		List<Week> weekList = HibernateUtil.listReuseSession(session, Week.class);
		assertNotNull("week list not received", weekList);
	}

	@Test
	public void testListSaveWeek() {
		List<Week> weekList = HibernateUtil.listReuseSession(session, Week.class);
		assertNotNull("week list not received", weekList);

		Week week3 = new Week();
		week3.setWeekNbrInYear(42);
		week3.setSchoolHolidaysWeek(Boolean.FALSE);
		week3.setBusinessWeek(Boolean.TRUE);
		HibernateUtil.saveReuseSession(session, week3);

	}

}
