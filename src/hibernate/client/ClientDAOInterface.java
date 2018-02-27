package hibernate.client;

import java.util.ArrayList;

import common.DropDownENT;
import common.client.ClientENT;
import tools.AMSException;


public interface ClientDAOInterface {
	public ArrayList<ClientENT> getAllClients(String searchKey) throws AMSException;
	public ClientENT getClient(int clientID) throws AMSException;
	public ArrayList<DropDownENT> getClientsDropDown() throws AMSException;
	public int getClientId(String username);
}
