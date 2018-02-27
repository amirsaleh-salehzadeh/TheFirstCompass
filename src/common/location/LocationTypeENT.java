package common.location;

import java.util.ArrayList;

public class LocationTypeENT {
	int locationTypeId;
	String locationType;
	LocationTypeENT parent;
	ArrayList<LocationTypeENT> children;
	
	public LocationTypeENT(int locationTypeId, String locationType,
			LocationTypeENT parent, ArrayList<LocationTypeENT> children) {
		super();
		this.locationTypeId = locationTypeId;
		this.locationType = locationType;
		this.parent = parent;
		this.children = children;
	}
	
	public LocationTypeENT(int locationTypeId, String locationType,
			LocationTypeENT parent) {
		super();
		this.locationTypeId = locationTypeId;
		this.locationType = locationType;
		this.parent = parent;
	}

	public LocationTypeENT getParent() {
		return parent;
	}

	public void setParent(LocationTypeENT parent) {
		this.parent = parent;
	}

	public ArrayList<LocationTypeENT> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<LocationTypeENT> children) {
		this.children = children;
	}

	public LocationTypeENT(int locationTypeId, String locationType) {
		super();
		this.locationTypeId = locationTypeId;
		this.locationType = locationType;
	}
	
	public LocationTypeENT() {
		super();
	}
	
	public LocationTypeENT(int locationTypeId) {
		super();
		this.locationTypeId = locationTypeId;
	}

	public int getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(int locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
}
