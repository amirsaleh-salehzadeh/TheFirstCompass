package hibernate.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import tools.AMSException;


public class BaseHibernateDAO {
	public static final int AMSEX_UNKNOWN = AMSException.AMSEX_UNKNOWN;
	public static final int AMSEX_DELETE = AMSException.AMSEX_DELETE;
	public static final int AMSEX_SAVE = AMSException.AMSEX_SAVE;
	public static final int AMSEX_SAVE_DUPLICATE = AMSException.AMSEX_SAVE_DUPLICATE;
	protected static final String DBADDRESS = "jdbc:mysql://localhost:3306/findmedb";
	protected static final String DBDRIVER = "com.mysql.jdbc.Driver";
	protected static final String USERNAME = "root";
	protected static final String PASSWORD = "";
	
//	private static final String DBADDRESS = "jdbc:mysql://postgrad.nmmu.ac.za:3306/nmmu_mobile";
//	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
//	private static final String USERNAME = "nmmu_mobile_user";
//	private static final String PASSWORD = "Dfjr9bg834gmb";

	//	LIVE SERVER NEVER USE IT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	protected static final String DBADDRESS = "jdbc:mysql://lnxsql-sc1.nmmu.ac.za:3306/findmedb?characterEncoding=utf8";
//	protected static final String DBDRIVER = "com.mysql.jdbc.Driver";
//	protected static final String USERNAME = "findmeuser";
//	protected static final String PASSWORD = "findM32017*&";
//	LIVE SERVER NEVER USE IT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//	public Session getSession(){
//		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		return sessionFactory.openSession();
//	}
	
	public Connection getConnection() throws AMSException {
		try {
			Class.forName(DBDRIVER);
		} catch (ClassNotFoundException e) {
			throw getAMSException(e.getMessage(), e);
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DBADDRESS, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw getAMSException(e.getMessage(), e);
		}
		return conn;
	}
	
//	public Session getSession4Query(){
//		return HibernateSessionFactory.getSession();
//	}
//	public Session getSession2Save(){
//		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		return sessionFactory.getCurrentSession();
//	}
	public AMSException getAMSException(int operationType, Exception ex) {
		String msg = ex.getMessage();
		switch (operationType) {
		case AMSEX_DELETE:
			msg = "A problem occured while deleting";
			break;
		case AMSEX_SAVE:
			msg = "A problem occured while saving";
			break;
		case AMSEX_SAVE_DUPLICATE:
			msg = "The record has been currently saved";
			
		}
		AMSException e = getAMSException(msg, ex);
		e.setType(operationType);
		return e;

	}
	public AMSException getAMSException(String defaultMessage, Exception ex) {
//		getSession().close();

		if (ex == null) {
			return new AMSException(defaultMessage);
		}

		ex.printStackTrace();
		// else if(ex.getMessage().startsWith(""){}
		return new AMSException(defaultMessage, ex);
	}
}
