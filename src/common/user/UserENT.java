package common.user;

import hibernate.client.ClientDAO;

import java.util.ArrayList;

import common.location.LocationENT;
import common.security.GroupENT;
import common.security.RoleENT;

public class UserENT {
	String userName;
	int clientID;
	String password;
	String registerationDate;
	String name;
	String surName;
	String dateOfBirth;
	boolean gender;
	boolean active;
	EthnicENT ethnic = new EthnicENT();
	int ethnicID;
	TitleENT title = new TitleENT();
	int titleID;
	ArrayList<RoleENT> roleENTs = new ArrayList<RoleENT>();
	ArrayList<GroupENT> groupENTs = new ArrayList<GroupENT>();
	ArrayList<LocationENT> locationENTs = new ArrayList<LocationENT>();

	public UserENT(String userName, int clientID, String password,
			String registerationDate, String name, String surName,
			String dateOfBirth, boolean gender, boolean active,
			EthnicENT ethnic, int ethnicID, TitleENT title, int titleID,
			ArrayList<RoleENT> roleENTs, ArrayList<GroupENT> groupENTs,
			ArrayList<LocationENT> locationENTs) {
		super();
		this.userName = userName;
		this.clientID = clientID;
		this.password = password;
		this.registerationDate = registerationDate;
		this.name = name;
		this.surName = surName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.active = active;
		this.ethnic = ethnic;
		this.ethnicID = ethnicID;
		this.title = title;
		this.titleID = titleID;
		this.roleENTs = roleENTs;
		this.groupENTs = groupENTs;
		this.locationENTs = locationENTs;
	}

	public UserENT(String userName) {
		super();
		this.userName = userName;
	}

	public UserENT(String userName, ArrayList<RoleENT> roleENTs,
			ArrayList<GroupENT> groupENTs, ArrayList<LocationENT> locationENTs) {
		super();
		this.userName = userName;
		this.roleENTs = roleENTs;
		this.groupENTs = groupENTs;
		this.locationENTs = locationENTs;
	}

	public UserENT(String userName, int clientID, String password) {
		super();
		this.userName = userName;
		this.clientID = clientID;
		this.password = password;
	}

	public UserENT() {
	}

	public void setRoleENTs(ArrayList<RoleENT> roleENTs) {
		this.roleENTs = roleENTs;
	}

	public ArrayList<GroupENT> getGroupENTs() {
		return groupENTs;
	}

	public void setGroupENTs(ArrayList<GroupENT> groupENTs) {
		this.groupENTs = groupENTs;
	}

	public ArrayList<LocationENT> getLocationENTs() {
		return locationENTs;
	}

	public void setLocationENTs(ArrayList<LocationENT> locationENTs) {
		this.locationENTs = locationENTs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		ClientDAO dao = new ClientDAO();
		if (this.clientID == 0)
			this.clientID = dao.getClientId(userName);
		this.userName = userName;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisterationDate() {
		return registerationDate;
	}

	public void setRegisterationDate(String registerationDate) {
		this.registerationDate = registerationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String sureName) {
		this.surName = sureName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public EthnicENT getEthnic() {
		return ethnic;
	}

	public void setEthnic(EthnicENT ethnic) {
		this.ethnic = ethnic;
		setEthnicID(ethnic.getEthnicID());
	}

	public int getEthnicID() {
		return ethnicID;
	}

	public void setEthnicID(int ethnicID) {
		this.ethnicID = ethnicID;
	}

	public int getTitleID() {
		return titleID;
	}

	public void setTitleID(int titleID) {
		this.titleID = titleID;
	}

	public ArrayList<RoleENT> getRoleENTs() {
		return roleENTs;
	}

	public TitleENT getTitle() {
		return title;
	}

	public void setTitle(TitleENT title) {
		this.title = title;
		setTitleID(title.getTitleID());
	}

}
