package common.location;

import java.util.ArrayList;

public class PathENT {
	LocationENT departure;
	LocationENT destination;
	LocationLightENT depL;
	LocationLightENT desL;
	double distance;
	ArrayList<PathTypeENT> pathTypes;
	String pathType;
	long pathId;
	String pathRoute;
	double width;
	String pathName;
	String description;

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
		this.pathTypes = new ArrayList<PathTypeENT>();
		String[] pathTypesArr = pathType.split(",");
		if (pathTypesArr != null && pathTypesArr.length > 0)
			for (int i = 0; i < pathTypesArr.length; i++)
				this.pathTypes.add(new PathTypeENT(Integer
						.parseInt(pathTypesArr[i])));
	}

	public String getPathRoute() {
		return pathRoute;
	}

	public void setPathRoute(String pathRoute) {
		this.pathRoute = pathRoute;
	}

	public PathENT() {
	}

	public ArrayList<PathTypeENT> getPathTypes() {
		return pathTypes;
	}

	public void setPathTypes(ArrayList<PathTypeENT> pathTypes) {
		this.pathTypes = pathTypes;
		if (pathTypes == null || pathTypes.size() <= 0)
			return;
		StringBuilder csvBuilder = new StringBuilder();
		for (PathTypeENT p : pathTypes) {
			csvBuilder.append(p.getPathTypeId());
			csvBuilder.append(",");
		}
		String csv = csvBuilder.toString();
		csv = csv.substring(0, csv.length() - 1);
		this.setPathType(csv);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PathENT(LocationENT departure, LocationENT destination,
			double distance, PathTypeENT pathType) {
		super();
		this.departure = departure;
		this.destination = destination;
		this.distance = distance;
		this.pathTypes = new ArrayList<PathTypeENT>();
		this.pathTypes.add(pathType);
	}

	public PathENT(LocationENT departure, LocationENT destination,
			double distance, PathTypeENT pathType, long pathId) {
		super();
		this.departure = departure;
		this.destination = destination;
		this.distance = distance;
		this.pathTypes = new ArrayList<PathTypeENT>();
		this.pathTypes.add(pathType);
		this.pathId = pathId;
	}

	public PathENT(LocationLightENT departure, LocationLightENT destination,
			double distance, PathTypeENT pathType, long pathId) {
		super();
		this.depL = departure;
		this.desL = destination;
		this.distance = distance;
		this.pathTypes = new ArrayList<PathTypeENT>();
		this.pathTypes.add(pathType);
		this.pathId = pathId;
	}

	public LocationLightENT getDepL() {
		return depL;
	}

	public void setDepL(LocationLightENT depL) {
		this.depL = depL;
	}

	public LocationLightENT getDesL() {
		return desL;
	}

	public void setDesL(LocationLightENT desL) {
		this.desL = desL;
	}

	public PathENT(long pathId) {
		super();
		this.pathId = pathId;
	}

	public PathENT(LocationENT departure, LocationENT destination) {
		super();
		this.departure = departure;
		this.destination = destination;
	}

	public PathENT(LocationENT departure, LocationENT destination,
			double distance, String pathType, long pathId, String pathRoute,
			double width, String pathName, String description) {
		super();
		this.departure = departure;
		this.destination = destination;
		this.distance = distance;
		this.pathType = pathType;
		this.pathTypes = new ArrayList<PathTypeENT>();
		String[] pathTypesArr = pathType.split(",");
		if (pathTypesArr != null && pathTypesArr.length > 0)
			for (int i = 0; i < pathTypesArr.length; i++)
				this.pathTypes.add(new PathTypeENT(Integer
						.parseInt(pathTypesArr[i])));
		this.pathId = pathId;
		this.pathRoute = pathRoute;
		this.width = width;
		this.pathName = pathName;
		this.description = description;
	}

	public long getPathId() {
		return pathId;
	}

	public void setPathId(long pathId) {
		this.pathId = pathId;
	}

	public PathENT(LocationENT departure, LocationENT destination,
			String pathType) {
		super();
		this.departure = departure;
		this.destination = destination;
		this.pathType = pathType;
		this.pathTypes = new ArrayList<PathTypeENT>();
		String[] pathTypesArr = pathType.split(",");
		if (pathTypesArr != null && pathTypesArr.length > 0)
			for (int i = 0; i < pathTypesArr.length; i++)
				this.pathTypes.add(new PathTypeENT(Integer
						.parseInt(pathTypesArr[i])));
	}

	public LocationENT getDeparture() {
		return departure;
	}

	public void setDeparture(LocationENT departure) {
		this.departure = departure;
	}
	
	public void setDepartureLocationId(long entranceId) {
		this.departure = new LocationENT(entranceId);
	}
	
	public void setDestinationLocationId(long entranceId) {
		this.destination = new LocationENT(entranceId);
	}

	public LocationENT getDestination() {
		return destination;
	}

	public void setDestination(LocationENT destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
