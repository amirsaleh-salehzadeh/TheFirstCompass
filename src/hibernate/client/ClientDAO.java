package hibernate.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.jersey.spi.inject.ClientSide;

import common.DropDownENT;
import common.client.ClientENT;
import common.security.GroupENT;
import common.security.RoleENT;
import common.user.UserENT;
import common.user.UserLST;
import common.user.UserPassword;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;

public class ClientDAO extends BaseHibernateDAO implements ClientDAOInterface {// implements
																				// ClientDAOInterface
																				// {

	public static void main(String[] args) {
		ClientDAO cdao = new ClientDAO();
		try {
			ArrayList<DropDownENT> clientENTs = cdao.getClientsDropDown();
			System.out.println(cdao.getClientId("admin"));
			System.out.println("");
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ClientENT> getAllClients(String searchKey)
			throws AMSException {
		ArrayList<ClientENT> clientENTs = new ArrayList<ClientENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from clients "
					+ " where client_name like '%" + searchKey + "%' ";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ClientENT c = new ClientENT(rs.getString("client_name"),
						rs.getInt("client_id"));
				clientENTs.add(c);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clientENTs;
	}

	public ClientENT getClient(int clientID) throws AMSException {
		// TODO Auto-generated method stub
		// Query q = null;
		ClientENT clientENT = new ClientENT("", clientID);
		// try {
		// q = getSession().createQuery(
		// "from ClientENT where clientID = :searchKey").setParameter(
		// "searchKey", clientID);
		// clientENT = (ClientENT) q.uniqueResult();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// }
		try {
			Connection con = getConnection();
			String query = "SELECT* FROM clients where client_id= ? ";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, clientID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				clientENT.setClientID(rs.getInt("client_id"));
				clientENT.setClientName(rs.getString("client_name"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clientENT;
	}

	public ArrayList<DropDownENT> getClientsDropDown() throws AMSException {
		ArrayList<DropDownENT> res = new ArrayList<DropDownENT>();
		try {
			Connection conn = null;
			List<ClientENT> dropdowns;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from clients ";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res.add(new DropDownENT(rs.getInt("client_id") + "", rs
						.getString("client_name"), null));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public int getClientId(String username) {
		Connection conn = null;
		int res = 0;
		try {
			conn = getConnection();
		} catch (AMSException e) {
			e.printStackTrace();
		}
		String query = "Select c.client_id from clients c"
				+ " inner join users u on u.client_id = c.client_id "
				+ " where u.username = '" + username + "' ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res = rs.getInt("client_id");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
