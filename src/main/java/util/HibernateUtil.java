package util;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	
	static{
		try{
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown(){
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
	
	/*public synchronized static void save(Object entity) {
		System.out.println("Saving " + entity.getClass());
		System.out.println("Object: " + entity);
		Session session = HibernateUtil.getSessionFactory().openSession();
		saveReuseSession(session, entity);
		session.close();
	}*/
	
	public synchronized static <E> List<E> listReuseSession(Session session, Class<E> clazz) {
		System.out.println("Listing " + clazz);

		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).list();
		tx.commit();
		return result;
	}
	
	public synchronized static <E> List<E> listReuseSessionOrderBy(Session session, Class<E> clazz, String colName, String fieldName) {
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
	
/*	public synchronized static <E> List<E> list(Class<E> clazz) {
		System.out.println("Listing " + clazz);
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<E> result = session.createCriteria(clazz).list();
		tx.commit();
		session.close();
		return result;
	}*/
}
