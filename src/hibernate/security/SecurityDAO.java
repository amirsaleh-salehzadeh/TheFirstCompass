package hibernate.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.tomcat.util.security.MD5Encoder;

import com.mysql.jdbc.Statement;

import common.security.GroupENT;
import common.security.GroupLST;
import common.security.RoleENT;
import common.security.RoleLST;
import common.user.UserPassword;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;
import tools.AMSUtililies;
import tools.MD5Encryptor;

public class SecurityDAO extends BaseHibernateDAO implements
		SecurityDAOInterface {

	public static void main(String[] args) {
		SecurityDAO udao = new SecurityDAO();
		try {
			RoleENT r = udao.getRole(new RoleENT("SuperAdmin"));
			UserPassword test = new UserPassword();
			test.setUserName("testersss");
			test.setUserPassword("number");
			udao.register(test);

			System.out.println(r.getComment());
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public RoleENT saveUpdateRole(RoleENT role, Connection conn)
			throws AMSException {
		boolean isnew = false;
		if (conn == null)
			try {
				conn = getConnection();
				isnew = true;
			} catch (AMSException e) {
				e.printStackTrace();
			}
		String query = "";
		query = "INSERT INTO roles (role_name, comment, category_role) "
				+ "VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE  comment = ?, category_role = ? ";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, role.getRoleName());
			ps.setString(2, role.getComment());
			ps.setString(3, role.getRoleCategory());
			ps.setString(4, role.getComment());
			ps.setString(5, role.getRoleCategory());
			ps.executeUpdate();
			ps.close();
			if (isnew)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return role;
	}

	public RoleENT getRole(RoleENT role) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from roles r where ";
			query += "role_name = ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, role.getRoleName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				role = new RoleENT(rs.getString("role_name"),
						rs.getString("category_role"), rs.getString("comment"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return role;
	}

	public RoleLST getRolesList(RoleLST roleLST) throws AMSException {
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "select * from roles";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			rs.last();
			roleLST.setTotalItems(rs.getRow());
			rs.close();
			ps.close();
			String searchKey = roleLST.getSearchRole().getRoleName();
			query = "Select r.* from roles r where "
					+ "r.role_name like ? or r.category_role like ? ";
			query += " order by " + roleLST.getSortedByField();
			if (roleLST.isAscending())
				query += ", role_name Asc";
			else
				query += " Desc";
			query += " LIMIT ?, ? ";
			ps = conn.prepareStatement(query);
			ps.setString(1, "%" + searchKey + "%");
			ps.setString(2, "%" + searchKey + "%");
			ps.setInt(3, roleLST.getFirst());
			ps.setInt(4, roleLST.getPageSize());
			rs = ps.executeQuery();
			while (rs.next()) {
				RoleENT r = new RoleENT(rs.getString("role_name"),
						rs.getString("category_role"), rs.getString("comment"));
				res.add(r);
			}
			rs.close();
			roleLST.setRoleENTs(res);
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roleLST;
	}

	public GroupLST getGroupList(GroupLST groupLST) throws AMSException {
		ArrayList<GroupENT> res = new ArrayList<GroupENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String searchKey = groupLST.getSearchGroup().getGroupName();
			String query = "Select g.*, c.client_name from groups g "
					+ "left join clients c on c.client_id = g.client_id where ";
			query += "g.group_name like ? ";
			query += " order by " + groupLST.getSortedByField();
			if (groupLST.isAscending())
				query += " Asc";
			else
				query += " Desc";
			query += " LIMIT ?, ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, "%" + searchKey + "%");
			ps.setInt(2, groupLST.getFirst());
			ps.setInt(3, groupLST.getPageSize());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GroupENT g = new GroupENT(rs.getInt("group_id"),
						rs.getString("group_name"), rs.getString("comment"));
				g.setClientName(rs.getString("client_name"));
				res.add(g);
			}
			rs.last();
			groupLST.setTotalItems(rs.getRow());
			rs.close();
			groupLST.setGroupENTs(res);
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupLST;
	}

	public GroupENT saveUpdateGroup(GroupENT group, Connection conn)
			throws AMSException {
		boolean isnew = false;
		if (conn == null)
			try {
				conn = getConnection();
				isnew = true;
			} catch (AMSException e) {
				e.printStackTrace();
			}
		String query = "";
		query = "insert into groups (group_name, comment, client_id)"
				+ " values (?, ?, ?)";
		if (group.getGroupID() > 0)
			query = "update groups set group_name= ?, comment = ?, client_id = ? where group_id = "
					+ group.getGroupID();
		PreparedStatement ps;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, group.getGroupName());
			ps.setString(2, group.getComment());
			ps.setInt(3, group.getClientID());
			if (group.getGroupID() > 0) {
				ps.setInt(4, group.getGroupID());
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					group.setGroupID(rs.getInt("group_id"));
				}
				rs.close();
			} else
				ps.executeUpdate();
			ps.close();
			if (isnew)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return group;
	}

	private boolean deleteGroup(GroupENT group) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "delete from groups where group_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, group.getGroupID());
			ps.execute();
			ps.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

	}

	public void saveUpdateRolesGroup(ArrayList<RoleENT> roles, GroupENT group)
			throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "delete from group_roles where group_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, group.getGroupID());
			ps.execute();
			query = "insert into group_roles (group_id, role_name) values (?,?)";
			ps.close();
			for (int i = 0; i < roles.size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setInt(1, group.getGroupID());
				ps.setString(2, roles.get(i).getRoleName());
				ps.execute();
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

	}

	public void changePassword(String oldPass, String newPass, String username)
			throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				throw getAMSException("", e);
			}
			String query = "select * from users where username = ? and password = ? ";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, AMSUtililies.encodeMD5(oldPass));
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw getAMSException("The old password does not match", null);
			}
			query = "update users set password = ? where username = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, AMSUtililies.encodeMD5(newPass));
			ps.setString(2, username);
			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

	}

	public ArrayList<String> getAllRoleCategories(String filter) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "Select distinct category_role from roles where category_role like '%"
					+ filter + "%'";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res.add(rs.getString("category_role"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public UserPassword register(UserPassword userPassword) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "";
			query = "select * from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, userPassword.getUserName());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				throw new AMSException("The username already exist");
			rs.close();
			query = "insert into users(username, password) values (?, ?)";
			ps.clearBatch();
			ps = conn.prepareStatement(query);
			ps.setString(1, userPassword.getUserName());
			ps.setString(2, MD5Encryptor.encode(userPassword.getUserPassword()));
			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException(AMSEX_SAVE, e);
		}
		return userPassword;
	}

	public boolean checkUsernameValidity(String userName) throws AMSException {
		Boolean ans = false;
		Connection con = null;
		try {
			con = getConnection();
			// checks if user in the database
			String query = "SELECT * FROM users where username=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			// if found then return true else return false
			if (rs.next()) {
				ans = true;
			}

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return ans;

	}

	public GroupENT getGroup(GroupENT group) throws AMSException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String query = "SELECT * FROM groups where group_id =  ?";

			ps = con.prepareStatement(query);
			ps.setInt(1, group.getGroupID());
			rs = ps.executeQuery();
			while (rs.next()) {
				GroupENT newone = new GroupENT(rs.getInt("group_id"),
						rs.getString("group_name"), rs.getInt("client_id"),
						rs.getString("comment"));
				ps.close();
				rs.close();
				return newone;
			}
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return new GroupENT();
	}

	public boolean deleteRoles(ArrayList<RoleENT> roles) throws AMSException {
		Connection con = null;
		Boolean happens = false;
		PreparedStatement ps = null;

		try {
			con = getConnection();
			for (RoleENT role : roles) {
				String query = " DELETE FROM roles where role_name = ?";
				ps = con.prepareStatement(query);
				ps.setString(1, role.getRoleName());
				happens = ps.execute();
				ps.close();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);

		}

		return happens;

	}

	public boolean deleteGroups(ArrayList<GroupENT> groups) throws AMSException {
		Boolean happens = false;
		try {
			for (GroupENT group : groups) {
				happens = deleteGroup(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);

		}

		return happens;
	}

	public ArrayList<RoleENT> getAllRolesForAGroup(int gid) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			// inner join query to get all the roles
			con = getConnection();
			String query = "SELECT g.* FROM group_roles g "
					+ "left join roles r on g.role_name = r.role_name"
					+ " where g.group_id = ? ";
			ps = con.prepareStatement(query);
			ps.setInt(1, gid);
			rs = ps.executeQuery();

			while (rs.next()) {
				res.add(new RoleENT(rs.getString("role_name")));

			}
			ps.close();
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public boolean saveGroupRole(GroupENT group) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from group_roles where group_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, group.getGroupID());
			ps.execute();
			query = "insert into group_roles (group_id, role_name) values (?,?)";
			for (int i = 0; i < group.getGroupRoles().size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setInt(1, group.getGroupID());
				ps.setString(2, group.getGroupRoles().get(i).getRoleName());
				ps.execute();
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return false;
	}

	public boolean changePassword(UserPassword ent) throws AMSException {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isUserAuthorised(String username, String role)
			throws AMSException {
		boolean isnew = false;
		boolean res = false;
		try {
			Class.forName(DBDRIVER);
			Connection conn = DriverManager.getConnection(DBADDRESS, USERNAME,
					PASSWORD);
			isnew = true;
			String query = "SELECT * FROM user_roles where username = ? and role_name = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, role);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				res = true;
			}
			rs.close();
			ps.close();
			if (isnew)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}

	
	public boolean addLocationUser(String username, long[] locationIds)  throws AMSException{
		try {
			Connection conn = null;
			try {
				conn = getConnection();
				conn.setAutoCommit(false);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "delete from user_location where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,username);
			ps.execute();
			ps.close();
			query = "insert into user_location (username,location_id) values (?,?)";
			for (int i = 0; i < locationIds.length; i++) {
				ps = conn.prepareStatement(query);
				ps.setString(1,username);
				ps.setLong(2,locationIds[i]);
				ps.execute();
			}
			ps.close();
			conn.commit();
			conn.close();
			return true ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public String getLocationUser(String username) throws AMSException {
		try {
			Class.forName(DBDRIVER);
			Connection conn = DriverManager.getConnection(DBADDRESS, USERNAME,
					PASSWORD);
			String query = "SELECT * FROM user_location where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			ArrayList<Long> ar = new ArrayList<Long>();
			while (rs.next()) {
				ar.add(rs.getLong("location_id"));
			}
			rs.close();
			ps.close();
			StringBuilder buil = new StringBuilder();
			if (ar.size() > 0) {
				for (long n : ar) {
					buil.append(n + "").append(",");
				}
				buil.deleteCharAt(buil.length() - 1);
			}
			String res = buil.toString();
			System.out.println(res);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public long getParentLocationUser(String username) throws AMSException {
		try {
			Class.forName(DBDRIVER);
			Connection conn = DriverManager.getConnection(DBADDRESS, USERNAME,
					PASSWORD);
			String query = "SELECT * FROM location l " +
					" left join users u on u.client_id = l.client_id " +
					" where parent_id = 0 and username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			long res = 0;
			if (rs.next()) {
				res = rs.getLong("location_id");
			}
			rs.close();
			ps.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
