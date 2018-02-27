package hibernate.route;

import java.util.ArrayList;

import common.DropDownENT;
import common.location.LocationENT;
import common.location.LocationLST;
import common.location.LocationTypeENT;
import common.location.PathENT;
import common.location.PathTypeENT;
import tools.AMSException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public interface PathDAOInterface {

	public ArrayList<PathENT> getRoutesForUserAndParent(String username,
			long parentId);

	public PathENT savePath(PathENT path, Connection conn);

	public boolean deletePath(PathENT ent, Connection conn) throws AMSException;

	public PathENT getAPath(PathENT ent, Connection conn);

	public ArrayList<PathENT> getShortestPath(long dep, long dest,
			int pathTypeId, String clientName, int areaId);

	public ArrayList<PathENT> createAPointOnPath(long pathId, String pointGPS,
			int index, long intersectionEntranceParentId, long departureId,
			String newPathRoute, String description, String pathName,
			int width, String pathType);

	public LocationENT findClosestLocation(String GPSCoordinates,
			String parentIds, String clientName);

	public long saveTrip(long deptLocationId, long destLocationId, int pathTypeId);

	public void deleteTrip(long tripId);

	public PathENT getTrip(long tripId);

	public ArrayList<PathENT> getAllPathsForOnePoint(long locationId, int type);

	public ArrayList<PathTypeENT> getAllPathTypes();

}
