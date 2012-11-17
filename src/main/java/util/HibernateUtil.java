package util;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;

	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			LOGGER.error("Problem creating the session Factory", e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

	public static Session createSession() {
		LOGGER.info("Creating session");
		Session session = HibernateUtil.getSessionFactory().openSession();
		return session;
	}

	public static void closeSession(Session session) {
		session.close();
	}

	public synchronized static void saveReuseSession(Session session, Object entity) {
		Transaction tx = session.beginTransaction();
		session.save(entity);
		tx.commit();
	}

	public synchronized static void saveOrUpdateReuseSession(Session session, Object entity) {
		LOGGER.info("Saving " + entity.getClass());
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(entity);
		tx.commit();
	}

	public synchronized static void saveOrUpdateReuseSession(Session session, List<? extends Object> entityList) {
		Transaction tx = session.beginTransaction();
		for (Object entity : entityList) {
			LOGGER.info("Saving " + entity.getClass());
			session.saveOrUpdate(entity);
		}
		tx.commit();
	}

	public synchronized static <E> List<E> listReuseSession(Session session, Class<E> clazz) {
		LOGGER.info("Listing " + clazz);
		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).list();
		tx.commit();
		return result;
	}

	public synchronized static <E> List<E> listReuseSessionOrderBy(Session session, Class<E> clazz, String colName,
			String fieldName) {
		LOGGER.info("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).createCriteria(colName).addOrder(Order.asc(fieldName)).list();
		tx.commit();
		return result;
	}

	public static void deleteReuseSession(Session session, Object entity) {
		Transaction tx = session.beginTransaction();
		session.delete(entity);
		tx.commit();

	}

	public static <E> List<E> listReuseSessionDateRangeOrderBy(Session session, Class<E> clazz, String colName,
			String fieldName, Date lower, Date upper, Boolean businessWeekOnly, Boolean holidaysOnly) {
		LOGGER.info("Listing " + clazz);

		Transaction tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(clazz).createCriteria(colName);

		if (businessWeekOnly != null && businessWeekOnly.booleanValue()) {
			criteria = criteria.add(Restrictions.gt("businessWeek", 0));
		}
		if (holidaysOnly != null) {
			criteria = criteria.add(Restrictions.eq("schoolHolidaysWeek", holidaysOnly));
		}
		if (fieldName != null) {
			criteria = criteria.addOrder(Order.asc(fieldName));
		}
		if (lower != null) {
			criteria = criteria.add(Restrictions.gt("startDate", lower));
		}
		if (upper != null) {
			criteria = criteria.add(Restrictions.lt("endDate", upper));
		}

		@SuppressWarnings("unchecked")
		List<E> result = criteria.list();
		tx.commit();
		return result;
	}

	public static <E> List<E> listReuseSessionDateRangeNMaxOrderBy(Session session, Class<E> clazz, String fieldName,
			Integer maxNbr, Date lower, Date upper, Boolean businessWeekOnly) {
		LOGGER.info("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(clazz).addOrder(Order.desc(fieldName)).createCriteria("week");
		if (businessWeekOnly != null && businessWeekOnly.booleanValue()) {
			criteria = criteria.add(Restrictions.gt("businessWeek", 0));
		}
		if (lower != null) {
			criteria = criteria.add(Restrictions.gt("startDate", lower));
		}
		if (upper != null) {
			criteria = criteria.add(Restrictions.lt("endDate", upper));
		}
		if (maxNbr != null) {
			criteria = criteria.setMaxResults(maxNbr);
		}
		@SuppressWarnings("unchecked")
		List<E> result = criteria.list();
		tx.commit();
		return result;
	}

	public synchronized static <E> E findReuseSession(Session session, Class<E> clazz, String id) {
		LOGGER.info("Finding " + clazz + " with id=" + id);
		Transaction tx = session.beginTransaction();

		E result = clazz.cast(session.get(clazz, id));
		tx.commit();
		return result;

	}

	public synchronized static <E> E findByField(Session session, Class<E> clazz, String fieldName, Object fieldValue) {
		LOGGER.info("Finding " + clazz + " with " + fieldName + " = " + fieldValue);
		Transaction tx = session.beginTransaction();
		E result = clazz.cast(session.createCriteria(clazz).add(Restrictions.naturalId().set(fieldName, fieldValue))
				.setCacheable(true).uniqueResult());
		tx.commit();
		return result;
	}

}
