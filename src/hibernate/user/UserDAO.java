package hibernate.user;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.security.MD5Encoder;

import com.mysql.jdbc.Statement;

import common.DropDownENT;
import common.client.ClientENT;
import common.security.GroupENT;
import common.security.RoleENT;
import common.user.EthnicENT;
import common.user.TitleENT;
import common.user.UserENT;
import common.user.UserLST;
import common.user.UserPassword;
import hibernate.config.BaseHibernateDAO;
import tools.AMSException;
import tools.AMSUtililies;
import tools.MD5Encryptor;

public class UserDAO extends BaseHibernateDAO implements UserDAOInterface {
	public static void main(String[] args) {
		UserDAO udao = new UserDAO();
		// try {
		// for (int i = 14; i < 20; i++) {
		// TitleENT roles = new TitleENT();
		// roles = udao.getTitle(2);
		// EthnicENT eth = new EthnicENT();
		// eth = udao.getEthnic(2);
		// UserENT ent = new UserENT();
		// ent.setActive(true);
		// ent.setClientID(1);
		// ent.setDateOfBirth("dob");
		// ent.setGender(true);
		// ent.setEthnic(eth);
		// ent.setRegisterationDate("today");
		// ent.setSurName("surnamezzz");
		// ent.setName("namezzzz"+i);
		// ent.setTitle(roles);
		// ent.setUserName("userNamezzzz4");
		// UserPassword up = new UserPassword();
		// up.setUserPassword("passsssss"+i);
		// ent.setPassword("pass"+i);
		// ent = udao.saveUpdateUser(ent);

		UserLST l = new UserLST();
		// UserENT us = new UserENT();
		// ArrayList<RoleENT> ad = new ArrayList<RoleENT>();
		// UserENT u = new UserENT();
		// u.setRoleENTs(ad);

		try {
			// udao.registerNewUser(new UserPassword("amir", "1234"));
			UserENT user = udao.getUserLST(l).getUserENTs().get(1);
			user.setName("tester");
			user.setSurName("maninjwa");
			udao.updateUserProfile(user);
			udao.getUserLST(new UserLST());

		} catch (AMSException e) {
			e.printStackTrace();
		}
		// UDAO.DELETEUSER(US);

		System.out.println("done");
	}

	public UserPassword registerNewUser(UserPassword ent) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
				conn.setAutoCommit(false);
			} catch (AMSException e) {
				e.printStackTrace();
			}

			String query = "";
			query = "select * from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, ent.getUserName());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				throw new AMSException("The username already exist");
			rs.close();
			ps.clearBatch();
			query = "insert into users (client_id,password,username,ethnic, title)"
					+ " values (2,?,?,1,1)";
			ps = conn.prepareStatement(query);
			ps = conn.prepareStatement(query);
			ps.setString(2, ent.getUserName());
			ps.setString(1, MD5Encryptor.encode(ent.getUserPassword()));
			ps.execute();
			ps.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return ent;
	}

	public UserLST getUserLST(UserLST lst) throws AMSException {
		ArrayList<UserENT> userENTs = new ArrayList<UserENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from users where client_id = " + lst.getSearchUser().getClientID();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				UserENT userENT = new UserENT(rs.getString("username"));
				userENT.setClientID(rs.getInt("client_id"));
				userENT.setName(rs.getString("name"));
				userENT.setSurName(rs.getString("surname"));
				userENT.setActive(rs.getBoolean("active"));
				userENT.setEthnicID(rs.getInt("ethnic"));
				userENT.setDateOfBirth(rs.getString("date_of_birth"));
				userENT.setGender(rs.getBoolean("gender"));
				userENT.setTitleID(rs.getInt("title"));
				userENT.setRegisterationDate(rs.getString("registeration_date"));
				userENT.setPassword(rs.getString("password"));
				userENTs.add(userENT);
			}
			rs.close();
			ps.close();
			conn.close();
			lst.setUserENTs(userENTs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lst;
	}

	public boolean deleteUser(UserENT user) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			ps.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public ArrayList<EthnicENT> getAllEthnics() throws AMSException {
		ArrayList<EthnicENT> list = new ArrayList<EthnicENT>();
		// Query q = null;
		// try {
		// q = getSession().createQuery("from EthnicENT");
		// list = (ArrayList<EthnicENT>) q.list();
		// HibernateSessionFactory.closeSession();
		// } catch (HibernateException ex) {
		// throw getAMSException("", ex);
		// }
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			String query = "SELECT * FROM ethnics";
			ps = con.prepareStatement(query);
			rs = ps.getResultSet();
			while (rs.next()) {
				EthnicENT newone = new EthnicENT();
				newone.setEthnic(rs.getString("ethnic"));
				newone.setEthnicID(rs.getInt("ethnic_id"));
				list.add(newone);

			}
			con.close();
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return list;
	}

	public ArrayList<TitleENT> getAllTitles() throws AMSException {
		ArrayList<TitleENT> list = new ArrayList<TitleENT>();
		// Query q = null;
		// try {
		// q = getSession().createQuery("from TitleENT");
		// list = (ArrayList<TitleENT>) q.list();
		// HibernateSessionFactory.closeSession();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// throw getAMSException("", ex);
		// }
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			String query = "SELECT * FROM titles";
			ps = con.prepareStatement(query);
			rs = ps.getResultSet();
			while (rs.next()) {
				TitleENT newone = new TitleENT();
				newone.setTitle(rs.getString("title"));
				newone.setTitleID(rs.getInt("title_id"));
				list.add(newone);

			}
			con.close();
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return list;

	}

	public EthnicENT getEthnic(int ethnicID) throws AMSException {
		// Query q = null;
		EthnicENT ethnic = new EthnicENT();
		// try {
		// q = getSession().createQuery("from EthnicENT where ethnicID =:Id");
		// q.setInteger("Id", ethnicID);
		// ethnic = (EthnicENT) q.uniqueResult();
		// HibernateSessionFactory.closeSession();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// throw getAMSException("", ex);
		// }
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			String query = "SELECT * FROM ethnics where ethnic_id = ?";
			ps = con.prepareStatement(query);
			ps.setInt(1, ethnicID);
			rs = ps.getResultSet();
			while (rs.next()) {

				ethnic.setEthnic(rs.getString("ethnic"));
				ethnic.setEthnicID(rs.getInt("ethnic_id"));
			}
			con.close();
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return ethnic;
	}

	public TitleENT getTitle(int titleID) throws AMSException {
		// Query q = null;
		TitleENT title = new TitleENT();
		// try {
		// q = getSession().createQuery("from TitleENT where titleID =:Id");
		// q.setInteger("Id", titleID);
		// title = (TitleENT) q.uniqueResult();
		// HibernateSessionFactory.closeSession();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// throw getAMSException("", ex);
		// }

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			String query = "SELECT * FROM titles where title_id = ?";
			ps = con.prepareStatement(query);
			ps.setInt(1, titleID);
			rs = ps.getResultSet();
			while (rs.next()) {

				title.setTitle(rs.getString("title"));
				title.setTitleID(rs.getInt("title_id"));

			}
			con.close();
			rs.close();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
		return title;
	}

	public UserENT getUserENT(UserENT user) throws AMSException {
		UserENT userENT = new UserENT();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "select * from users where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				userENT.setUserName(rs.getString("username"));
				userENT.setClientID(rs.getInt("client_id"));
				userENT.setName(rs.getString("name"));
				userENT.setSurName(rs.getString("surname"));
				userENT.setActive(rs.getBoolean("active"));
				userENT.setDateOfBirth(rs.getString("date_of_birth"));
				userENT.setEthnicID(rs.getInt("ethnic"));
				userENT.setTitleID(rs.getInt("title"));
				userENT.setRegisterationDate(rs.getString("registeration_date"));
				userENT.setPassword(rs.getString("password"));
				if (rs.getInt("gender") == 1) {
					userENT.setGender(true);
				} else
					userENT.setGender(false);

				// Please set all attributes for a users here...
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userENT;
	}

	public ArrayList<DropDownENT> getTitlesDropDown() {
		// Query q = null;
		ArrayList<DropDownENT> res = new ArrayList<DropDownENT>();
		// try {
		// Session s = getSession4Query();
		// s.beginTransaction();
		// List<TitleENT> dropdowns = getSession4Query().createQuery(
		// "from TitleENT").list();
		// for (TitleENT dropdown : dropdowns) {
		// res.add(new DropDownENT(dropdown.getTitleID() + "", dropdown
		// .getTitle(), null));
		// }
		// // List dropDown = q.list();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// }
		return res;
	}

	public ArrayList<DropDownENT> getEthnicsDropDown() {
		// Query q = null;
		ArrayList<DropDownENT> res = new ArrayList<DropDownENT>();
		// try {
		// Session s = getSession4Query();
		// s.beginTransaction();
		// List<EthnicENT> dropdowns = getSession4Query().createQuery(
		// "from EthnicENT").list();
		// for (EthnicENT dropdown : dropdowns) {
		// res.add(new DropDownENT(dropdown.getEthnicID() + "", dropdown
		// .getEthnic(), null));
		// }
		// // List dropDown = q.list();
		// } catch (HibernateException ex) {
		// ex.printStackTrace();
		// }

		return res;
	}

	public ArrayList<RoleENT> getAllRolesUser(String username) {
		ArrayList<RoleENT> res = new ArrayList<RoleENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from user_roles where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RoleENT r = new RoleENT(rs.getString("role_name"));
				res.add(r);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<GroupENT> getAllGroupsUser(String username) {
		ArrayList<GroupENT> res = new ArrayList<GroupENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "Select ug.*, g.* from user_groups ug"
					+ " inner join groups g on g.group_id = ug.group_id"
					+ " inner join users u on u.username = ug.username "
					+ " and u.username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GroupENT g = new GroupENT(rs.getInt("group_id"),
						rs.getString("group_name"), rs.getString("comment"));
				res.add(g);
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void saveUpdateUserRoles(UserENT user) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
				conn.setAutoCommit(false);
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "delete from user_roles where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			query = "insert into user_roles (username, role_name) values (?,?)";
			for (int i = 0; i < user.getRoleENTs().size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getRoleENTs().get(i).getRoleName());
				ps.execute();
			}
			ps.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public boolean deleteUsers(ArrayList<UserENT> users) throws AMSException {
		for (int i = 0; i < users.size(); i++) {
			deleteUser(users.get(i));
		}
		return true;
	}

	public void saveUpdateUserGroups(UserENT user) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from user_groups where username = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUserName());
			ps.execute();
			query = "insert into user_groups (group_id, username) values (?,?)";
			for (int i = 0; i < user.getGroupENTs().size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setString(2, user.getUserName());
				ps.setInt(1, user.getGroupENTs().get(i).getGroupID());
				ps.execute();
				query = "insert into user_roles (group_id, username) values (?,?)";
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public UserENT updateUserProfile(UserENT user) throws AMSException {
		String query = "";
		PreparedStatement ps = null;
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			query = "Insert into users ( active, client_id, date_of_birth, ethnic, gender, name, surname, title, username, password)" +
					" values (?,?,?,?,?,?,?,?,?,?) on duplicate key " +
					"UPDATE active= ? , client_id = ? , date_of_birth = ? , ethnic = ? , gender = ?"
					+ ", name = ?, registeration_date = ?, surname = ?, title = ? "
					+ " ";
			ps = conn.prepareStatement(query);
			if (user.isActive()) {
				ps.setInt(1, 1);
			} else
				ps.setInt(1, 0);
			ps.setInt(2, user.getClientID());
			ps.setString(3, user.getDateOfBirth());
			ps.setInt(4, 1);
			if (user.isGender()) {
				ps.setInt(5, 1);
			} else
				ps.setInt(5, 0);
			ps.setString(6, user.getName());
			ps.setString(7, user.getSurName());
			ps.setInt(8, 1);
			ps.setString(9, user.getUserName());
			ps.setString(10, MD5Encryptor.encode(user.getPassword()));
			if (user.isActive()) {
				ps.setInt(11, 1);
			} else
				ps.setInt(11, 0);

			ps.setInt(12, user.getClientID());
			ps.setString(13, user.getDateOfBirth());
			ps.setInt(14, 1);
			if (user.isGender()) {
				ps.setInt(15, 1);
			} else
				ps.setInt(15, 0);

			ps.setString(16, user.getName());
//			ps.setString(7, user.getPassword());
			ps.setString(17, user.getRegisterationDate());
			ps.setString(18, user.getSurName());
			ps.setInt(19, 1);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw getAMSException("", e);
		}

		return user;

	}

}
