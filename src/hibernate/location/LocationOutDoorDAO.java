package hibernate.location;

import hibernate.config.BaseHibernateDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import tools.AMSException;

import common.location.LocationENT;
import common.location.LocationTypeENT;

public class LocationOutDoorDAO extends BaseHibernateDAO implements
		LocationOutDoorDAOInterface {

//	public LocationENT getLocationENTChildren(LocationENT parent,
//			String clientName) {
//		LocationENT ent = null;
//		try {
//			Connection conn = null;
//			try {
//				conn = getConnection();
//			} catch (AMSException e) {
//				e.printStackTrace();
//			}
//			String query = "";
//			query = "select l.*, lt.location_type_id, lt.location_type ltname from location l"
//					+ " left join location_type lt on lt. location_type_id = l.location_type"
//					+ " where l.username = '"
//					+ clientName
//					+ "' and l.location_id = "
//					+ parent.getLocationID()
//					+ " and l.location_type != 5 order by l.location_id asc";
//			PreparedStatement ps = conn.prepareStatement(query);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				ent = new LocationENT(rs.getLong("location_id"),
//						rs.getString("username"), new LocationTypeENT(
//								rs.getInt("location_type_id"),
//								rs.getString("ltname")),
//						 rs.getString("gps"),
//						rs.getString("location_name"));
//				ent.setIcon(rs.getString("icon"));
//				ent.setDescription(rs.getString("description"));
//				ent.setParentId(rs.getLong("parent_id"));
//				ent.setChildrenENT(getLocationENTTree(ent, conn));
//			}
//			ps.close();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return ent;
//	}

//	private ArrayList<LocationENT> getLocationENTTree(LocationENT ent,
//			Connection conn) {
//		ArrayList<LocationENT> res = new ArrayList<LocationENT>();
//		try {
//			String query = "";
//			query = "select l.*, lt.location_type ltname from location l"
//					+ " left join location_type lt on lt. location_type_id = l.location_type"
//					+ " where l.location_type != 5 and l.parent_id = "
//					+ ent.getLocationID() + " order by l.location_id asc";
//			PreparedStatement ps = conn.prepareStatement(query);
//			ResultSet rs = ps.executeQuery();
//			boolean end = false;
//			while (rs.next()) {
//				end = true;
//				ent = new LocationENT(rs.getLong("location_id"),
//						rs.getString("username"), new LocationTypeENT(
//								rs.getInt("location_type"),
//								rs.getString("ltname")),
//						 rs.getString("gps"),
//						rs.getString("location_name"));
//				ent.setIcon(rs.getString("icon"));
//				ent.setParentId(rs.getLong("parent_id"));
//				ent.setDescription(rs.getString("description"));
//				ent.setChildrenENT(getLocationENTTree(ent, conn));
//				res.add(ent);
//			}
//			ps.close();
//			// conn.close();
//			if (!end)
//				return null;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}

}
