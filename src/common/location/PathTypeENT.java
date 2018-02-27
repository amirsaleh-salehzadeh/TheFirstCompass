package common.location;

public class PathTypeENT {
	int pathTypeId;
	String pathType;

	public PathTypeENT(int pathTypeId, String pathType) {
		super();
		this.pathTypeId = pathTypeId;
		this.pathType = pathType;
	}
	
	public PathTypeENT(int pathTypeId) {
		super();
		this.pathTypeId = pathTypeId;
	}

	public int getPathTypeId() {
		return pathTypeId;
	}

	public void setPathTypeId(int pathTypeId) {
		this.pathTypeId = pathTypeId;
	}

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
	}

}
