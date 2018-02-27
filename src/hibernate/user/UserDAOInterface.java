package hibernate.user;

import java.util.ArrayList;

import common.DropDownENT;
import common.security.GroupENT;
import common.security.RoleENT;
import common.user.EthnicENT;
import common.user.TitleENT;
import common.user.UserENT;
import common.user.UserLST;
import common.user.UserPassword;
import tools.AMSException;


public interface UserDAOInterface {
	public UserPassword registerNewUser(UserPassword ent) throws AMSException;
	public UserLST getUserLST(UserLST lst) throws AMSException;
	public UserENT getUserENT(UserENT user) throws AMSException;
	public boolean deleteUser(UserENT user) throws AMSException;
	public ArrayList<EthnicENT> getAllEthnics() throws AMSException;
	public ArrayList<TitleENT> getAllTitles() throws AMSException;
	public EthnicENT getEthnic(int ethnicID) throws AMSException;
	public TitleENT getTitle(int titleID) throws AMSException;
	public ArrayList<RoleENT> getAllRolesUser(String username);
	public ArrayList<GroupENT> getAllGroupsUser(String username);
	public void saveUpdateUserRoles(UserENT user) throws AMSException;
	public UserENT updateUserProfile(UserENT user) throws AMSException;
	public void saveUpdateUserGroups(UserENT user) throws AMSException;
	public ArrayList<DropDownENT> getTitlesDropDown();
	public ArrayList<DropDownENT> getEthnicsDropDown();
	public boolean deleteUsers(ArrayList<UserENT> users) throws AMSException;
	//activate user
}
