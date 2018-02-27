package common.events;

import java.math.BigInteger;
import java.util.Date;

public class EventENT {
	private String eventId;
	private String username;
	private BigInteger creatorUId;
	private String description;
	private Date endTime;
	private Date startTime;
	private String eventTitle;
	private int price;
	private int status;
	private boolean active;

	/**
	 * @return the eventId
	 */

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEventId() {
		return eventId;
	}

	/**
	 * @param eventId
	 *            the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the creatorUId
	 */
	public BigInteger getCreatorUId() {
		return creatorUId;
	}

	/**
	 * @param creatorUId
	 *            the creatorUId to set
	 */
	public void setCreatorUId(BigInteger creatorUId) {
		this.creatorUId = creatorUId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the eventTitle
	 */
	public String getEventTitle() {
		return eventTitle;
	}

	/**
	 * @param eventTitle
	 *            the eventTitle to set
	 */
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}