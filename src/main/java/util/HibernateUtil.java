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

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

	public static Session createSession() {
		System.out.println("Creating session");
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
		System.out.println("Saving " + entity.getClass());
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(entity);
		tx.commit();
	}

	public synchronized static <E> List<E> listReuseSession(Session session, Class<E> clazz) {
		System.out.println("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).list();
		tx.commit();
		return result;
	}

	public synchronized static <E> List<E> listReuseSessionOrderBy(Session session, Class<E> clazz, String colName,
			String fieldName) {
		System.out.println("Listing " + clazz);

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
			String fieldName, Date lower, Date upper) {
		System.out.println("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).createCriteria(colName).addOrder(Order.asc(fieldName))
		.add(Restrictions.gt("startDate", lower)).add(Restrictions.lt("endDate", lower)).list();
		tx.commit();
		return result;
	}

	public static <E> List<E> listReuseSessionDateRangeNMaxOrderBy(Session session, Class<E> clazz, String fieldName,
			Integer maxNbr, Date lower, Date upper) {
		System.out.println("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(clazz).addOrder(Order.desc(fieldName)).createCriteria("week");
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

}
