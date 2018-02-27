package common.security;

import java.util.ArrayList;

public class GroupENT {
	int groupID;
	String groupName = "";
	int clientID;
	String clientName;
	ArrayList<RoleENT> groupRoles;

	public GroupENT() {

	}

	public GroupENT(int groupID, String groupName, int clientID,
			String clientName, ArrayList<RoleENT> groupRoles, String comment) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.clientID = clientID;
		this.clientName = clientName;
		this.groupRoles = groupRoles;
		this.comment = comment;
	}

	public GroupENT(int groupID, String groupName, int clientID, String comment) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.clientID = clientID;
		this.comment = comment;
	}
	
	public GroupENT(int groupID, String groupName, String comment) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.comment = comment;
	}

	public ArrayList<RoleENT> getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(ArrayList<RoleENT> groupRoles) {
		this.groupRoles = groupRoles;
	}

	public GroupENT(int groupID) {
		super();
		this.groupID = groupID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	String comment = "";

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

}
