package common.events;
import java.util.List;


/**
 * The persistent class for the events_category database table.
 * 
 */
public class EventCategory {
	private int categoryId;
	private String title;
	private List<EventENT> eventENTs;
	private List<EventCategory> eventsCategoryChildren;

	public EventCategory() {
	}

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the eventENTs
	 */
	public List<EventENT> getEventENTs() {
		return eventENTs;
	}

	/**
	 * @param eventENTs the eventENTs to set
	 */
	public void setEventENTs(List<EventENT> eventENTs) {
		this.eventENTs = eventENTs;
	}

	/**
	 * @return the eventsCategoryChildren
	 */
	public List<EventCategory> getEventsCategoryChildren() {
		return eventsCategoryChildren;
	}

	/**
	 * @param eventsCategoryChildren the eventsCategoryChildren to set
	 */
	public void setEventsCategoryChildren(List<EventCategory> eventsCategoryChildren) {
		this.eventsCategoryChildren = eventsCategoryChildren;
	}
	
}