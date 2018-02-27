package common.security;

public class RoleENT {
	String roleName = "";
	String roleCategory;
	String comment = "";

	public String getRoleCategory() {
		return roleCategory;
	}

	public void setRoleCategory(String roleCategory) {
		this.roleCategory = roleCategory;
	}



	public RoleENT() {

	}
	
	public RoleENT(String roleName, String roleCategory, String comment) {
		super();
		this.roleName = roleName;
		this.roleCategory = roleCategory;
		this.comment = comment;
	}

	public RoleENT(String roleName) {
		super();
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
