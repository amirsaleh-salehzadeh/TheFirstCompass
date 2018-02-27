package common.location;

public class LevelENT {
	long levelId;
	long parent_id;
	String description;
	long planId;
	
	public LevelENT(long levelId) {
		super();
		this.levelId = levelId;
	}
	
	public LevelENT(long levelId, long parent_id, String description,
			long planId) {
		super();
		this.levelId = levelId;
		this.parent_id = parent_id;
		this.description = description;
		this.planId = planId;
	}
	/**
	 * @return the levelId
	 */
	public long getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(long levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return the parent_id
	 */
	public long getParent_id() {
		return parent_id;
	}
	/**
	 * @param parent_id the parent_id to set
	 */
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
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
	 * @return the planId
	 */
	public long getPlanId() {
		return planId;
	}
	/**
	 * @param planId the planId to set
	 */
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	
}
