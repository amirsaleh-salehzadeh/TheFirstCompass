package hibernate.events;

import hibernate.config.BaseHibernateDAO;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import tools.AMSException;

import common.events.EventCategory;
import common.events.EventENT;

public class EventDAO extends BaseHibernateDAO implements EventDAOInterface {
	

	@Override
	public void deleteEvent(EventENT ent) throws AMSException {
		// TODO Auto-generated method stub
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from events where event_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, ent.getEventId());
			ps.execute();
			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	@Override
	public EventENT createUpdateEvent(EventENT ent ,Connection conn) throws AMSException {
		// TODO Auto-generated method stub
		try {
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean isnew = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isnew = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			 String query = " insert into events(event_title, description, start_time, end_time, status," +
					" creator_username, price,active) " +
					"values(?,?,?,?,?,?,?,?)"; 

			if (ent.getEventId() != null) {
				query = "update events set event_title = ? ,description=? ,start_time = ? ,end_time =?"
						+ "status =? ,creator_username=? ,price=?, active=?"
						+ "where event_id = ?"; // table field is
												// creator_username which is a
				
			}// string
				ps = conn.prepareStatement(query);
				ps.setString(1, ent.getEventTitle());
				ps.setString(2, ent.getDescription());
				ps.setDate(3,(Date) ent.getStartTime()); // date format
															// yyyy-mm-dd
															// hh-mm-ss
				ps.setDate(4, (Date) ent.getEndTime());
				ps.setInt(5, ent.getStatus());
				ps.setString(6, ent.getUsername());
				ps.setInt(7, ent.getPrice());

				if (ent.isActive()) {
					ps.setInt(8, 1);
				} else
					ps.setInt(8, 1);
				
				if(ent.getEventId()!=null)
				ps.setString(9, ent.getEventId());

			
			ps.execute();
			ps.close();
			if (isnew) {
				conn.commit();
				conn.close();
			}
			return ent;

		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

	}

	@Override
	public ArrayList<EventENT> getAllEvents(EventENT ent) throws AMSException {

		// TODO Auto-generated method stub
		ArrayList<EventENT> events = new ArrayList<>();
		try {
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "select * from events where start_time <= ? and end_time >= ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDate(1, (Date) ent.getStartTime());
			ps.setDate(2, (Date) ent.getEndTime());

			ps.execute();
			rs = ps.getResultSet();
			while (rs.next()) {
				EventENT event = new EventENT();
				event.setUsername(rs.getString("creator_username")); // table field is
															// creator_username
															// which is a string
				event.setDescription(rs.getString("description"));
				event.setEndTime(rs.getDate("end_time"));
				event.setStartTime(rs.getDate("start_time"));
				event.setEventTitle(rs.getString("event_title"));
				event.setPrice(rs.getInt("price"));
				event.setStatus(rs.getInt("status"));
				if (rs.getInt("active") == 1) {
					event.setActive(true);
				} else
					event.setActive(false);
				event.setEventId(rs.getString("event_id"));
				events.add(ent);
			}

			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return events;

	}

	@Override
	public ArrayList<EventCategory> getAllEventCategories() throws AMSException {
		// TODO Auto-generated method stub
		ArrayList<EventCategory> categories = new ArrayList<>();
		try {
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "select * from events_category";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			rs = ps.getResultSet();
			while (rs.next()) {
				EventCategory ent = new EventCategory();
				ent.setCategoryId(rs.getInt("category_id"));
				ent.setTitle(rs.getString("title"));
				ent.setEventsCategoryChildren(getChildrenCategories(ent));
				categories.add(ent);
			}
			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return categories;
	}

	private ArrayList<EventCategory> getChildrenCategories(EventCategory ent)
			throws AMSException {
		ArrayList<EventCategory> categories = new ArrayList<>();
		try {
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "select * from events_category where parent_id =?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, ent.getCategoryId());
			ps.execute();
			rs = ps.getResultSet();
			while (rs.next()) {
				EventCategory child = new EventCategory();
				child.setCategoryId(rs.getInt("category_id"));
				child.setTitle(rs.getString("title"));
				categories.add(ent);
			}

			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return categories;

	}

	
}
