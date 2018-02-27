package hibernate.route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.mysql.jdbc.Statement;

import common.location.EntranceIntersectionENT;
import common.location.LocationENT;
import common.location.LocationLightENT;
import common.location.LocationTypeENT;
import common.location.PathENT;
import common.location.PathTypeENT;
import graph.management.GraphGenerator;
import hibernate.config.BaseHibernateDAO;
import hibernate.location.LocationDAO;
import tools.AMSException;

public class PathDAO extends BaseHibernateDAO implements PathDAOInterface {

	public long saveTrip(long deptLocationId, long destLocationId,
			int pathTypeId) {
		long res = 0;
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			query = "insert into trips (departure_location_id, destination_location_id, path_type_id)"
					+ " values (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, deptLocationId);
			ps.setLong(2, destLocationId);
			ps.setInt(3, pathTypeId);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				res = rs.getLong(1);
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void deleteTrip(long tripId) {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String query = "delete from trips where trip_id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, tripId);
			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PathENT getTrip(long tripId) {
		PathENT res = new PathENT();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from trips where trip_id = " + tripId;
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// res = new PathENT(getLocationENT(
				// new LocationENT(rs.getLong("departure_location_id")),
				// conn), getLocationENT(
				// new LocationENT(rs.getLong("destination_location_id")),
				// conn));
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public LocationENT findClosestLocation(String GPSCoordinates,
			String parentIds, String clientName) {
		LocationDAO dao = new LocationDAO();
		LocationENT ent = new LocationENT();
		ent.setCoordinates(GPSCoordinates);
		ent = dao.getLocationENT(ent, null);
		if (ent.getLocationID() > 0)
			return ent;
		ArrayList<LocationENT> points = dao.getAllLocationAndEntrancesForUser(
				clientName, parentIds);
		int counter = 0;
		for (int i = 0; i < points.size(); i++)
			for (int k = 0; k < points.get(i).getEntrances().size(); k++)
				counter++;

		int closest = -1;
		double[] distances = new double[counter];
		for (int i = 0; i < points.size(); i++)
			for (int k = 0; k < points.get(i).getEntrances().size(); k++) {
				distances[i + k] = PathDAO.calculateDistanceBetweenTwoPoints(
						points.get(i).getEntrances().get(k).getCoordinates(),
						GPSCoordinates);
				if (closest == -1 || distances[i] < distances[closest]) {
					closest = i;
				}
			}
		int closestEntrance = -1;
		for (int i = 0; i < points.get(closest).getEntrances().size(); i++) {
			distances[i] = PathDAO.calculateDistanceBetweenTwoPoints(points
					.get(closest).getEntrances().get(i).getCoordinates(),
					GPSCoordinates);
			if (closestEntrance == -1
					|| distances[i] < distances[closestEntrance]) {
				closestEntrance = i;
			}
		}
		// for (int i = 0; i < points.get(closest).getEntrances().size(); i++) {
		// if (closestEntrance != i) {
		// points.get(closest).getEntrances().set(i, null);
		// } else
		// points.get(closest).getEntrances()
		// .set(0, points.get(closest).getEntrances().get(closestEntrance));
		// }
		ArrayList<EntranceIntersectionENT> tm = new ArrayList<EntranceIntersectionENT>();
		tm.add(points.get(closest).getEntrances().get(closestEntrance));
		points.get(closest).setEntrances(tm);
		return points.get(closest);
	}

	public ArrayList<PathENT> getRoutesForUserAndParent(String username,
			long parentId) {
		ArrayList<PathENT> res = new ArrayList<PathENT>();
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (AMSException e) {
			e.printStackTrace();
		}
		try {
			LocationDAO ldao = new LocationDAO();
			// String paString = ldao.getLocationENT(new LocationENT(parentId),
			// null).getParentId()
			// + "," + parentId;
			String query = "Select p.*, pt.*, GROUP_CONCAT(ppt.path_type_id) as pathtypeString from path p "
					// +
					// "inner join location_entrance edep on edep.entrance_id = p.departure_location_id "
					+ "inner join location_entrance edes on edes.entrance_id = p.destination_location_id "
					+ " left join location lf on lf.location_id = edes.parent_id "
					+ " left join path_path_type ppt on ppt.path_id = p.path_id"
					+ " left join path_type pt on pt.path_type_id = ppt.path_type_id"
					+ " left join clients c on c.client_id = lf.client_id"
					+ " left join users u on u.client_id = c.client_id"
					+ " where u.username = '"
					+ username
					+ "' group by p.path_id";

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocationENT depl = ldao.getEntranceLocation(
						ldao.getEntranceIntersectionENT(
								rs.getLong("departure_location_id"), conn),
						conn);
				LocationENT desl = ldao.getEntranceLocation(
						ldao.getEntranceIntersectionENT(
								rs.getLong("destination_location_id"), conn),
						conn);
				PathENT ent = new PathENT(depl, desl, rs.getDouble("distance"),
						rs.getString("pathtypeString"), rs.getLong("path_id"),
						rs.getString("path_route"), rs.getDouble("width"),
						rs.getString("path_Name"), rs.getString("description"));
				res.add(ent);
			}
			ps.close();
			conn.close();
			// if (res.size() > 0)
			// res.addAll(getRoutesForUserAndParent(username, res.get(0)
			// .getPathId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public PathENT savePath(PathENT path, Connection conn) {
		try {
			boolean isnew = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isnew = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "insert into path (destination_location_id, departure_location_id, distance, path_route, path_name, description, width)"
					+ " select ?, ?, ?, ?, ?, ?, ? from path where not exists (select * from path where "
					+ "(destination_location_id = ? and departure_location_id = ?) or (departure_location_id = ? and destination_location_id = ?)) limit 1";
			LocationDAO dao = new LocationDAO();
			LocationENT entDep = dao.getEntranceLocation(
					new EntranceIntersectionENT(path.getDeparture()
							.getLocationID()), conn);
			LocationENT entDes = dao.getEntranceLocation(
					new EntranceIntersectionENT(path.getDestination()
							.getLocationID()), conn);
			if (path.getDistance() <= 0)
				path.setDistance(calculateDistance(entDep.getEntrances().get(0)
						.getCoordinates(), entDes.getEntrances().get(0).getCoordinates(),
						path.getPathRoute()));
			if (path.getPathId() > 0)
				query = "update path set destination_location_id = ?, departure_location_id = ?, "
						+ "distance = ?, path_route = ?,  path_name = ?, description = ?, width = ?"
						+ " where path_id = ?";
			PreparedStatement ps = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			if (path.getPathId() > 0) {
				ps.setLong(1, path.getDestination().getLocationID());
				ps.setLong(2, path.getDeparture().getLocationID());
				ps.setDouble(
						3,
						reEvaluateDistance(path.getDistance(),
								path.getPathTypes()));
				ps.setString(4, path.getPathRoute());
				ps.setString(5, path.getPathName());
				ps.setString(6, path.getDescription());
				ps.setDouble(7, path.getWidth());
				ps.setLong(8, path.getPathId());
			} else {
				ps.setLong(1, path.getDestination().getLocationID());
				ps.setLong(2, path.getDeparture().getLocationID());
				ps.setDouble(
						3,
						reEvaluateDistance(path.getDistance(),
								path.getPathTypes()));
				ps.setString(4, path.getPathRoute());
				ps.setString(5, path.getPathName());
				ps.setString(6, path.getDescription());
				ps.setDouble(7, path.getWidth());
				ps.setLong(8, path.getDestination().getLocationID());
				ps.setLong(9, path.getDeparture().getLocationID());
				ps.setLong(10, path.getDestination().getLocationID());
				ps.setLong(11, path.getDeparture().getLocationID());
			}
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				path.setPathId(rs.getLong(1));
			}
			ps.close();
			path = savePathTypes(path, conn);
			path.setDeparture(dao.getEntranceLocation(
					new EntranceIntersectionENT(path.getDeparture()
							.getLocationID()), conn));
			path.setDestination(dao.getEntranceLocation(
					new EntranceIntersectionENT(path.getDestination()
							.getLocationID()), conn));
			if (isnew) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return path;
	}

	public static double calculateDistanceBetweenTwoPoints(String gps,
			String gps2) {
		final int R = 6371;
		double latDistance = Math
				.toRadians(Double.parseDouble(gps2.split(",")[0])
						- Double.parseDouble(gps.split(",")[0]));
		double lonDistance = Math
				.toRadians(Double.parseDouble(gps2.split(",")[1])
						- Double.parseDouble(gps.split(",")[1]));
		double a = Math.sin(latDistance / 2)
				* Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(Double.parseDouble(gps.split(",")[0])))
				* Math.cos(Math.toRadians(Double.parseDouble(gps2.split(",")[0])))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double outp = (double) Double.parseDouble(new DecimalFormat(".##")
				.format(R * c * 1000));
		return outp;
	}

	public static double calculateDistance(String departure,
			String destination, String pathRoute) {
		double outp = 0;
		if (pathRoute != null && pathRoute.length() > 1) {
			String[] points = pathRoute.split("_");
			if (points.length > 1) {
				for (int i = 0; i < points.length; i++) {
					if (i == 0)
						outp += calculateDistanceBetweenTwoPoints(departure,
								points[i]);
					else
						outp += calculateDistanceBetweenTwoPoints(
								points[i - 1], points[i]);
				}
				outp += calculateDistanceBetweenTwoPoints(destination,
						points[points.length - 1]);
			} else {
				outp += calculateDistanceBetweenTwoPoints(departure, pathRoute);
				outp += calculateDistanceBetweenTwoPoints(pathRoute,
						destination);
			}
		} else {
			outp = calculateDistanceBetweenTwoPoints(destination, departure);
		}
		outp = (double) Double.parseDouble(new DecimalFormat(".###")
				.format(outp));
		return outp;
	}

	private double reEvaluateDistance(double distance,
			ArrayList<PathTypeENT> pathTypes) {
		for (int i = 0; i < pathTypes.size(); i++) {
			int pathTypeId = pathTypes.get(i).getPathTypeId();
			if (pathTypeId == 6)// stairways
				distance += 2.00;
		}
		return distance;
	}

	public PathENT savePathTypes(PathENT path, Connection conn) {
		try {
			boolean isnew = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isnew = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "delete from path_path_type where path_id = "
					+ path.getPathId();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			query = "insert into path_path_type (path_id, path_type_id)"
					+ " select ? , ? from path_path_type WHERE NOT EXISTS (select * from path_path_type where path_id = ? and path_type_id = ?) limit 1";
			for (int i = 0; i < path.getPathTypes().size(); i++) {
				ps = conn.prepareStatement(query);
				ps.setLong(1, path.getPathId());
				ps.setInt(2, path.getPathTypes().get(i).getPathTypeId());
				ps.setLong(3, path.getPathId());
				ps.setInt(4, path.getPathTypes().get(i).getPathTypeId());
				ps.executeUpdate();
				ps.close();
			}
			if (isnew) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return path;
	}

	public boolean deletePath(PathENT ent, Connection conn) throws AMSException {
		try {
			boolean isNewCon = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isNewCon = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			// ent = getAPath(ent, conn);
			// LocationDAO dao = new LocationDAO();
			// long depId = ent.getDeparture().getEntrances().get(0)
			// .getEntranceId();
			// long desId = ent.getDestination().getEntrances().get(0)
			// .getEntranceId();
			// ArrayList<PathENT> pz = getAllPathsForOnePoint(depId, 1);
			// if (pz.size() == 0) {
			// try {
			// dao.deleteEntrance(new EntranceIntersectionENT(depId), conn);
			// } catch (AMSException e) {
			// e.printStackTrace();
			// }
			// }
			// if (pz.size() == 1) {
			// deletePath(pz.get(0), conn);
			// dao.deleteEntrance(new EntranceIntersectionENT(depId), conn);
			// }
			// pz = getAllPathsForOnePoint(desId, 1);
			// if (pz.size() == 0) {
			// try {
			// dao.deleteEntrance(new EntranceIntersectionENT(desId), conn);
			// } catch (AMSException e) {
			// e.printStackTrace();
			// }
			// }
			// if (pz.size() == 1) {
			// deletePath(pz.get(0), conn);
			// dao.deleteEntrance(new EntranceIntersectionENT(desId), conn);
			// }
			String query = "";
			query = "delete from path_path_type where path_id = "
					+ ent.getPathId();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			query = "delete from path where path_id = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, ent.getPathId());
			ps.executeUpdate();
			// checkForExtraIntersection(ent.getPathId());
			ps.close();
			if (isNewCon) {
				conn.commit();
				conn.close();
			}
			return true;
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw getAMSException("", e);
		}
	}

	public PathENT getAPath(PathENT ent, Connection conn) {
		try {
			boolean isNewCon = false;
			if (conn == null)
				try {
					conn = getConnection();
					conn.setAutoCommit(false);
					isNewCon = true;
				} catch (AMSException e) {
					e.printStackTrace();
				}
			String query = "";
			query = "select * from path where path_id = " + ent.getPathId();
			if (ent.getDeparture() != null
					&& ent.getDeparture().getLocationID() > 0
					&& ent.getDestination() != null
					&& ent.getDestination().getLocationID() > 0)
				query = "select * from path where (departure_location_id = "
						+ ent.getDeparture().getLocationID()
						+ " and destination_location_id = "
						+ ent.getDestination().getLocationID()
						+ ") or (destination_location_id = "
						+ ent.getDeparture().getLocationID()
						+ " and departure_location_id = "
						+ ent.getDestination().getLocationID() + ")";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			LocationDAO dao = new LocationDAO();
			while (rs.next()) {
				LocationENT dep = dao.getEntranceLocation(
						new EntranceIntersectionENT(rs
								.getLong("departure_location_id")), conn);
				dep.setParent(null);
				dep.setIcon(null);
				LocationENT des = dao.getEntranceLocation(
						new EntranceIntersectionENT(rs
								.getLong("destination_location_id")), conn);
				des.setParent(null);
				des.setIcon(null);
				ent = new PathENT(dep, des);
				ent.setPathRoute(rs.getString("path_route"));
				ent.setPathId(rs.getLong("path_id"));
				ent.setPathTypes(getPathTypesOfAPath(rs.getLong("path_id"),
						conn));
				ent.setWidth(rs.getDouble("width"));
				ent.setPathName(rs.getString("path_Name"));
				ent.setDescription(rs.getString("description"));
				ent.setDesL(new LocationLightENT(des));
				ent.setDepL(new LocationLightENT(dep));
				ent.setDestination(des);
				ent.setDeparture(dep);
			}
			ps.close();
			if (isNewCon) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return ent;
	}

	private ArrayList<PathTypeENT> getPathTypesOfAPath(long pathId,
			Connection conn) {
		ArrayList<PathTypeENT> res = new ArrayList<PathTypeENT>();
		boolean isNewCon = false;
		if (conn == null)
			try {
				conn = getConnection();
				isNewCon = true;
			} catch (AMSException e) {
				e.printStackTrace();
			}
		String query = "select ppt.*, p.path_type from path_path_type ppt "
				+ " left join path_type p on p.path_type_id = ppt.path_type_id "
				+ " where path_id = " + pathId;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res.add(new PathTypeENT(rs.getInt("path_type_id"), rs
						.getString("path_type")));
			}
			ps.close();
			if (isNewCon)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	public ArrayList<PathENT> getShortestPath(long dep, long dest,
			int pathTypeId, String clientName, int areaId) {
		ArrayList<PathENT> res = new ArrayList<PathENT>();
		try {
			UndirectedGraph<Long, DefaultWeightedEdge> graph = null;
			GraphGenerator g = new GraphGenerator();
			if (g.fetchGraph(clientName, areaId, pathTypeId) == null)
				g.generateGraph(clientName, areaId, pathTypeId);
			graph = g.fetchGraph(clientName, areaId, pathTypeId);
			// DijkstraShortestPath<Long, DefaultWeightedEdge> dsp = new
			// DijkstraShortestPath<Long, DefaultWeightedEdge>(
			// graph, dep, dest);
			List<DefaultWeightedEdge> shortest_path = DijkstraShortestPath
					.findPathBetween(graph, dep, dest);
			if (shortest_path == null)
				shortest_path = new ArrayList<DefaultWeightedEdge>();
			for (int i = 0; i < shortest_path.size(); i++) {
				long source = graph.getEdgeSource(shortest_path.get(i));
				long target = graph.getEdgeTarget(shortest_path.get(i));
				PathENT tmpPath = getAPath(new PathENT(new LocationENT(source),
						new LocationENT(target)), null);
				tmpPath.setDestination(null);
				tmpPath.setDeparture(null);
				LocationLightENT srcLoc = tmpPath.getDepL();
				LocationLightENT tarLoc = tmpPath.getDesL();
				String pathRoute = tmpPath.getPathRoute();
				// System.out.println(srcLoc.id);
				String resPathRoute = "";
				if (i == 0 && source != dep) {
					long tmp = source;
					source = target;
					target = tmp;
					LocationLightENT tmpLoc = srcLoc;
					srcLoc = tarLoc;
					tarLoc = tmpLoc;
					if (pathRoute != null && pathRoute.length() > 0) {
						String[] tmpPathRoute = pathRoute.split("_");
						for (int j = tmpPathRoute.length - 1; j >= 0; j--) {
							if (j != 0)
								resPathRoute += tmpPathRoute[j] + "_";
							else
								resPathRoute += tmpPathRoute[j];
						}
					}
				} else if (i > 0)
					if (source != res.get(i - 1).getDesL().getId()) {
						long tmp = source;
						source = target;
						target = tmp;
						LocationLightENT tmpLoc = srcLoc;
						srcLoc = tarLoc;
						tarLoc = tmpLoc;
						if (pathRoute != null && pathRoute.length() > 0) {
							String[] tmpPathRoute = pathRoute.split("_");
							for (int j = tmpPathRoute.length - 1; j >= 0; j--) {
								if (j != 0)
									resPathRoute += tmpPathRoute[j] + "_";
								else
									resPathRoute += tmpPathRoute[j];
							}
						}
					}
				if (resPathRoute.length() > 0)
					tmpPath.setPathRoute(resPathRoute);
				tmpPath.setDesL(tarLoc);
				tmpPath.setDepL(srcLoc);
				// System.out.println(tarLoc.getId() + " " + srcLoc + " " +
				// tmpPath.getDistance());
				// if (tmpPath.getPathType().indexOf("9") == -1 || res.size() ==
				// 0)
				res.add(tmpPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public ArrayList<PathENT> getAllPathsForOnePoint(long locationId, int type) {
		ArrayList<PathENT> res = new ArrayList<PathENT>();
		Connection conn = null;
		try {
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "";
			// if (type == 0)
			query = "Select DISTINCT p.* from path p "
					+ " inner join path_path_type ppt on ppt.path_id = p.path_id "
					+ "where ppt.path_type_id in ("
					+ returnPathTypeIdsComma(type)
					+ ") and (p.destination_location_id = " + locationId
					+ " or p.departure_location_id = " + locationId + ")";
			// System.out.println(locationId);
			// else
			// query = "Select * from path where path_type != " + type
			// + " and (destination_location_id = '" + locationId
			// + "' or departure_location_id = '" + locationId + "')";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			LocationDAO dao = new LocationDAO();
			while (rs.next()) {
				LocationENT dep = dao.getEntranceLocation(
						new EntranceIntersectionENT(rs
								.getLong("departure_location_id")), conn);
				LocationENT des = dao.getEntranceLocation(
						new EntranceIntersectionENT(rs
								.getLong("destination_location_id")), conn);
				PathENT ent = new PathENT(dep, des);
				ent.setPathRoute(rs.getString("path_route"));
				ent.setPathId(rs.getLong("path_id"));
				ent.setPathTypes(getPathTypesOfAPath(rs.getLong("path_id"),
						conn));
				ent.setWidth(rs.getDouble("width"));
				ent.setDistance(rs.getDouble("distance"));
				ent.setPathName(rs.getString("path_Name"));
				ent.setDescription(rs.getString("description"));
				res.add(ent);
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	private String returnPathTypeIdsComma(int type) {
		String res = "";
		switch (type) {
		case 1: // DIRT ROAD
			res = "1,2,3,4,5,6,7,8,9";
			break;
		case 2:// WALKAWAY
			res = "2,3,4,5,6,7,8,9";
			break;
		case 5:// DRIVE
			res = "5";
			break;
		case 6:// WHEELCHAIR
			res = "4,5,6,8,9";
			break;
		case 8:// INDOOR
			res = "8,9";
			break;
		case 9:// INDOOR
			res = "9";
			break;
		default:
			res = type + "";
			break;
		}
		return res;
	}

	public static UndirectedGraph<Long, DefaultWeightedEdge> createGraph(
			int pathTypeId, String clientName) {
		SimpleWeightedGraph<Long, DefaultWeightedEdge> g = null;
		g = new SimpleWeightedGraph<Long, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		PathDAO pdao = new PathDAO();
		ArrayList<LocationENT> points = getAllPointsForGraph(clientName);
		for (int i = 0; i < points.size(); i++) {
			long depTMP = points.get(i).getEntrances().get(0).getEntranceId();
			ArrayList<PathENT> ptz = pdao.getAllPathsForOnePoint(depTMP,
					pathTypeId);
			// if (!g.containsVertex(depTMP) && ptz.size() > 0)
			// g.addVertex(depTMP);
			for (int j = 0; j < ptz.size(); j++) {
				long destTMP = ptz.get(j).getDestination().getEntrances()
						.get(0).getEntranceId();
				depTMP = ptz.get(j).getDeparture().getEntrances().get(0)
						.getEntranceId();
				if (!g.containsVertex(destTMP)) {
					g.addVertex(destTMP);
				}
				if (!g.containsVertex(depTMP)) {
					g.addVertex(depTMP);
				}
				DefaultWeightedEdge edg = g.addEdge(depTMP, destTMP);
				if (edg != null) {
					g.setEdgeWeight(edg, ptz.get(j).getDistance());
					if (!g.containsVertex(destTMP)) {
						g.addVertex(destTMP);
					}
					if (!g.containsVertex(depTMP)) {
						g.addVertex(depTMP);
					}
					// System.out.println(depTMP + " " + destTMP + " " + " " +
					// ptz.get(j).getDistance());
				}
			}
		}
		return g;
	}

	private static ArrayList<LocationENT> getAllPointsForGraph(String clientName) {
		ArrayList<LocationENT> res = new ArrayList<LocationENT>();
		try {
			Connection conn = null;
			try {
				Class.forName(DBDRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(DBADDRESS, USERNAME, PASSWORD);
			String query = "Select * from location_entrance";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			LocationDAO dao = new LocationDAO();
			while (rs.next()) {
				res.add(dao.getEntranceLocation(
						new EntranceIntersectionENT(rs.getLong("entrance_id")),
						conn));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<PathTypeENT> getAllPathTypes() {
		ArrayList<PathTypeENT> res = new ArrayList<PathTypeENT>();
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "Select * from path_type";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PathTypeENT p = new PathTypeENT(rs.getInt("path_type_id"),
						rs.getString("path_type"));
				res.add(p);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<PathENT> createAPointOnPath(long pathId, String pointGPS,
			int index, long intersectionEntranceParentId, long departureId,
			String newPathRoute, String description, String pathName,
			int width, String pathType)

	{
		ArrayList<PathENT> res = new ArrayList<PathENT>();
		Connection conn = null;
		final PathENT p = getAPath(new PathENT(pathId), null);
		final long depId = p.getDeparture().getEntrances().get(0)
				.getEntranceId();
		final long desId = p.getDestination().getEntrances().get(0)
				.getEntranceId();
		EntranceIntersectionENT ent = new EntranceIntersectionENT();
		ent.setCoordinates(pointGPS);
		ent.setDescription("Intersection");
		ent.setParentId(intersectionEntranceParentId);
		ent.setEntranceIntersection(false);
		LocationDAO dao = new LocationDAO();
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			ent = dao.saveEntrance(ent, conn);
			String[] points = p.getPathRoute().split("_");
			StringJoiner firstRoute = new StringJoiner("_");
			StringJoiner secondRoute = new StringJoiner("_");
			for (int i = 0; i < points.length; i++) {
				if (i < index - 1)
					firstRoute.add(points[i]);
				else
					secondRoute.add(points[i]);
			}
			PathENT path = new PathENT();
			path.setPathName(pathName);
			path.setPathRoute(newPathRoute);
			path.setPathType(pathType);
			path.setDepartureLocationId(departureId);
			path.setDestinationLocationId(ent.getEntranceId());
			path.setWidth(width);
			path.setDescription(description);
			path.setDistance(calculateDistance(ent.getCoordinates(), dao
					.getEntranceIntersectionENT(departureId, conn).getCoordinates(),
					newPathRoute));
			path = savePath(path, conn);
			res.add(getAPath(new PathENT(path.getPathId()), conn));

			path = new PathENT();
			path = p;
			path.setPathId(0);
			path.setDeparture(new LocationENT(depId));
			path.setDestination(new LocationENT(ent.getEntranceId()));// dao.getEntranceLocation(ent,
			path.setPathRoute(firstRoute.toString());
			path = savePath(path, conn);
			res.add(getAPath(new PathENT(path.getPathId()), conn));

			path.setPathId(0);
			path.setDeparture(new LocationENT(ent.getEntranceId()));
			path.setDestination(new LocationENT(desId));
			path.setPathRoute(secondRoute.toString());
			path = savePath(path, conn);
			res.add(getAPath(new PathENT(path.getPathId()), conn));
			deletePath(new PathENT(pathId), conn);

			// path.setPathId(0);
			// path.setDeparture(new LocationENT(depId));
			// path.setDestination(new LocationENT(ent.getEntranceId()));
			// path.setPathRoute(firstRoute.toString());
			// path = savePath(path, conn);
			// res.add(getAPath(new PathENT(path.getPathId()), conn));
			// path.setPathId(0);
			// path.setDeparture(new LocationENT(ent.getEntranceId()));
			// path.setDestination(new LocationENT(desId));
			// path.setPathRoute(secondRoute.toString());
			// path = savePath(path, conn);
			// res.add(getAPath(new PathENT(path.getPathId()), conn));
			// deletePath(new PathENT(pathId), conn);

			conn.commit();
			conn.close();
		} catch (AMSException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return new ArrayList<PathENT>();
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return new ArrayList<PathENT>();
		}
		return res;
	}

}