package hibernate.events;

import java.sql.Connection;
import java.util.ArrayList;



import common.events.EventCategory;
import common.events.EventENT;
import tools.AMSException;


public interface EventDAOInterface {
	public void deleteEvent(EventENT ent) throws AMSException;
	public EventENT createUpdateEvent(EventENT ent ,Connection conn) throws AMSException;
	public ArrayList<EventENT> getAllEvents(EventENT entn) throws AMSException;
	public ArrayList<EventCategory> getAllEventCategories() throws AMSException;
}