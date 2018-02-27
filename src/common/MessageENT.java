package common;

public class MessageENT {
	String success = "";
	String error = "";

	public MessageENT(String success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
