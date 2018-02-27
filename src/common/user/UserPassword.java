package common.user;

public class UserPassword {
	private String userName;
	private String userPassword;

	public UserPassword(String userName, String userPassword) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	public UserPassword() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
