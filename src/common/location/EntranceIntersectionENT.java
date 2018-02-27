package common.location;

public class EntranceIntersectionENT {
	long entranceId;
	long parentId;
	String description;
	String coordinates;
	boolean entranceIntersection;
	boolean disabilityFriendly;
	boolean isLocation;
	boolean emergencyExit;
	boolean isPrivate;
	
	public EntranceIntersectionENT(long parentId,
			String description, String coordinates, boolean entranceIntersection,
			boolean disabilityFriendly, boolean isLocation,
			boolean emergencyExit, boolean isPrivate) {
		super();
		this.parentId = parentId;
		this.description = description;
		this.coordinates = coordinates;
		this.entranceIntersection = entranceIntersection;
		this.disabilityFriendly = disabilityFriendly;
		this.isLocation = isLocation;
		this.emergencyExit = emergencyExit;
		this.isPrivate = isPrivate;
	}

	/**
	 * @return the disabilityFriendly
	 */
	public boolean isDisabilityFriendly() {
		return disabilityFriendly;
	}

	/**
	 * @param disabilityFriendly the disabilityFriendly to set
	 */
	public void setDisabilityFriendly(boolean disabilityFriendly) {
		this.disabilityFriendly = disabilityFriendly;
	}

	/**
	 * @return the isLocation
	 */
	public boolean isLocation() {
		return isLocation;
	}

	/**
	 * @param isLocation the isLocation to set
	 */
	public void setLocation(boolean isLocation) {
		this.isLocation = isLocation;
	}

	/**
	 * @return the emergencyExit
	 */
	public boolean isEmergencyExit() {
		return emergencyExit;
	}

	/**
	 * @param emergencyExit the emergencyExit to set
	 */
	public void setEmergencyExit(boolean emergencyExit) {
		this.emergencyExit = emergencyExit;
	}

	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public EntranceIntersectionENT() {
		super();
	}
	
	public EntranceIntersectionENT(long entranceId, long parentId,
			String description, String coordinates, boolean entranceIntersection) {
		super();
		this.entranceId = entranceId;
		this.parentId = parentId;
		this.description = description;
		this.coordinates = coordinates;
		this.entranceIntersection = entranceIntersection;
	}

	/**
	 * @return the entranceIntersection
	 */
	public boolean isEntranceIntersection() {
		return entranceIntersection;
	}

	/**
	 * @param entranceIntersection the entranceIntersection to set
	 */
	public void setEntranceIntersection(boolean entranceIntersection) {
		this.entranceIntersection = entranceIntersection;
	}

	public EntranceIntersectionENT(long entranceId) {
		super();
		this.entranceId = entranceId;
	}
	
	public EntranceIntersectionENT(long entranceId, long parentId, String description,
			String coordinates) {
		super();
		this.entranceId = entranceId;
		this.parentId = parentId;
		this.description = description;
		this.coordinates = coordinates;
	}
	/**
	 * @return the entranceId
	 */
	public long getEntranceId() {
		return entranceId;
	}
	/**
	 * @param entranceId the entranceId to set
	 */
	public void setEntranceId(long entranceId) {
		this.entranceId = entranceId;
	}
	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the gps
	 */
	public String getCoordinates() {
		return coordinates;
	}
	/**
	 * @param gps the gps to set
	 */
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
}
