package tools;

public class AMSException extends Exception {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public static final int AMSEX_UNKNOWN = 0;
	public static final int AMSEX_DELETE = 1;
	public static final int AMSEX_SAVE = 2;
	public static final int AMSEX_SAVE_DUPLICATE = 3;

	private int type = 0;

	public AMSException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}

	public AMSException(String message, Throwable cause) {
		// TODO Auto-generated constructor stub
		super(message, cause);
	}

	public AMSException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super(cause);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}