package common.client;

public class ClientENT {
	String clientName;
	int clientID;

	public ClientENT(String clientName, int clientID) {
		super();
		this.clientName = clientName;
		this.clientID = clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

}
