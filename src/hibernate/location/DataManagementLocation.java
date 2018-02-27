package hibernate.location;

import hibernate.config.BaseHibernateDAO;
import hibernate.route.PathDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import tools.AMSException;

import common.location.EntranceIntersectionENT;
import common.location.LocationENT;
import common.location.PathENT;

public class DataManagementLocation extends BaseHibernateDAO {

	private static void updateAllDescriptions() {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> all = dao.getAllLocationsForUser("NMMU", "3,5",
				null);
		for (int i = 0; i < all.size(); i++) {
			LocationENT ent = all.get(i);

			// UPDATE DESCRIPTION

			String descString = ent.getLocationName();
			final String res = StringEscapeUtils.unescapeXml(descString + " &");
			if (ent.getLocationName().contains("401"))
				System.out.println(res);

			// if (descString != null) {
			// String[] tmpSentence = descString.split(" ");
			// for (int j = 0; j < tmpSentence.length; j++) {
			// if (tmpSentence[j].length() <= 3)
			// continue;
			// tmpSentence[j] = tmpSentence[j].toLowerCase();
			// // .toUpperCase()
			// if (j < tmpSentence.length)
			// res += tmpSentence[j].substring(0, 1).toUpperCase()
			// + tmpSentence[j].substring(1) + " ";
			// }
			// }

			ent.setDescription(res);
			if (res.length() <= 1)
				ent.setDescription(null);
			if (ent.getIcon() != null && ent.getIcon().length() < 5)
				ent.setIcon(null);
			// try {
			// dao.saveUpdateLocation(ent);
			// } catch (AMSException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	private static void updateAllGPS() {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> all = dao.getAllLocationsForUser("NMMU", "", "");
		for (int i = 0; i < all.size(); i++) {
			LocationENT ent = all.get(i);
			String res = "";

			// UPDATE GPS
			String descString = ent.getCoordinates();
			System.out.println(descString);
			if (descString != null) {
				String[] tmpSentence = descString.split(",");
				// for (int j = 0; j < tmpSentence.length; j++) {
				double x = Double.parseDouble(tmpSentence[0]);
				double y = (double) Double.parseDouble(new DecimalFormat(
						".#######").format(x));
				res += y;
				x = Double.parseDouble(tmpSentence[1]);
				y = (double) Double.parseDouble(new DecimalFormat(".#######")
						.format(x));
				res += "," + y;
				// }
			}
			System.out.println(res);
			// ent.setDescription(res);
			ent.setCoordinates(res);
			if (res.length() <= 1)
				ent.setDescription(null);
			if (ent.getIcon() != null && ent.getIcon().length() < 5)
				ent.setIcon(null);
			try {
				dao.saveUpdateLocation(ent, null);
			} catch (AMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void updateAllGPSParents(String topLeft, String topRight,
			String bottomRight, String BottomLeft) {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> ents = dao.getAllLocationsForUser("NMMU", "3,5",
				"");
		double latTL = Double.parseDouble(topLeft.split(",")[0]);
		double lngTL = Double.parseDouble(topLeft.split(",")[1]);
		double latBL = Double.parseDouble(BottomLeft.split(",")[0]);
		double lngBL = Double.parseDouble(BottomLeft.split(",")[1]);
		double latTR = Double.parseDouble(topRight.split(",")[0]);
		double lngTR = Double.parseDouble(topRight.split(",")[1]);
		double latBR = Double.parseDouble(bottomRight.split(",")[0]);
		double lngBR = Double.parseDouble(bottomRight.split(",")[1]);
		for (int i = 0; i < ents.size(); i++) {
			LocationENT ent = ents.get(i);
			if (ent.getLocationID() == 360)
				continue;
			String[] gps = ent.getCoordinates().split(",");
			double lat = Double.parseDouble(gps[0]);
			double lng = Double.parseDouble(gps[1]);
			if (lat < latTL && lng > lngTL && lat < latTR && lng < lngTR
					&& lat > latBL && lng > lngBL && lat > latBR && lng < lngBR) {
				ent.setParentId(371);
				System.out.println(ent.getLocationName() + " "
						+ ent.getLocationID());
				try {
					dao.saveUpdateLocation(ent, null);
				} catch (AMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateAPathLength(PathENT path) throws AMSException {
		try {
			Connection conn = null;
			try {
				conn = getConnection();
			} catch (AMSException e) {
				e.printStackTrace();
			}
			String query = "update path set distance = " + path.getDistance()
					+ " where path_id = " + path.getPathId();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<PathENT> getAllPaths() throws AMSException {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (AMSException e) {
			e.printStackTrace();
		}
		ArrayList<PathENT> res = new ArrayList<PathENT>();
		try {
			String query = "Select * from path p "
					+ "left join location_entrance le on le.entrance_id = p.departure_location_id "
					+ " left join location_entrance le2 on le2.entrance_id = p.destination_location_id ";
			// +
			// "where le.is_location = 0 and le2.is_location = 0";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			LocationDAO ldao = new LocationDAO();
			while (rs.next()) {
				PathENT ent = new PathENT(ldao.getEntranceLocation(
						new EntranceIntersectionENT(rs
								.getLong("departure_location_id")), conn),
						ldao.getEntranceLocation(
								new EntranceIntersectionENT(rs
										.getLong("destination_location_id")),
								conn), rs.getDouble("distance"), "1",
						rs.getLong("path_id"), rs.getString("path_route"), 2,
						"", "");
				res.add(ent);
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	private static void updatePointsToBoundary() {
		LocationDAO dao = new LocationDAO();

		ArrayList<LocationENT> ents = dao.getAllLocationsForUser("NMMU", "3",
				"");
		for (int i = 0; i < ents.size(); i++) {
			float[] pXy = new float[2];
			// dao.saveEntrance(new EntranceIntersectionENT(ents.get(i)
			// .getLocationID(), ents.get(i).getParentId(), ents.get(i)
			// .getLocationName(), ents.get(i).getGps(), false), null);
			if (ents.get(i).getBoundary() != null
					&& ents.get(i).getBoundary().length() > 10) {
				ArrayList<EntranceIntersectionENT> rnt = dao
						.getEntrancesForALocation(ents.get(i), null, false);
				String[] tmp = ents.get(i).getBoundary().split(";")[0]
						.split("_");
				float[][] aXys = new float[tmp.length][2];
				for (int j = 0; j < aXys.length; j++) {
					aXys[j][0] = Float.parseFloat(tmp[j].split(",")[0]);
					aXys[j][1] = Float.parseFloat(tmp[j].split(",")[1]);
				}
				for (int j = 0; j < rnt.size(); j++) {
					pXy[0] = Float
							.parseFloat(rnt.get(j).getCoordinates().split(",")[0]);
					pXy[1] = Float
							.parseFloat(rnt.get(j).getCoordinates().split(",")[0]);
					System.out.println(ents.get(i).getLocationName());
					dao.saveEntrance(
							new EntranceIntersectionENT(ents.get(i)
									.getLocationID(), ents.get(i)
									.getLocationID(), ents.get(i)
									.getLocationName(), dao
									.getClosestPointOnLines(pXy, aXys), true),
							null);
				}
			}
		}
	}

	public static void main(String[] args) {
		// updateAllGPS();
		DataManagementLocation daomng = new DataManagementLocation();

		// try {
		// daomng.getAllPaths();
		// } catch (AMSException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// updateAllDescriptions();

		// NORTH CAMPUS NMMU
		// updateAllGPSParents("-33.996924, 25.665436",
		// "-33.9972578,25.6763445",
		// "-34.0045724,25.6792518", "-34.0053476,25.6648867");

		// 2ndAVE
		// updateAllGPSParents("-33.986996,25.651153", "-33.984546,25.664695",
		// "-33.990612,25.665772", "-33.991390,25.654094");

		// Bird Street
		// updateAllGPSParents("-33.964712,25.615733", "-33.964339,25.618636",
		// "-33.966208,25.619162", "-33.966277,25.616377");

		// Missionvale
		// updateAllGPSParents("-33.870589,25.549132", "-33.870016,25.555359",
		// "-33.874217,25.556063", "-33.875109,25.549600");

		// George Campus
		// updateAllGPSParents("-33.9438158,22.5002528",
		// "-33.9420745,22.5411127",
		// "-33.987197,22.5592594", "-33.991323,22.5072237");

		//
		// updatePointsToBoundary();
		System.out.println("start");
		LocationDAO dao = new LocationDAO();

		// UPDATE ALL DISTANCES
		updateAllDistances();

		// updateAllDescriptions();
		// daomng.removeUnwantedIntersections();
		// daomng.findNotConnectedBuildings();
		// daomng.addLocationEntranceForBuildings();
		// PathDAO pdao = new PathDAO();
		// ArrayList<PathENT> pz = pdao.getAllPathsForOnePoint(1525, 1);
		// for (int i = 0; i < pz.size(); i++) {
		// System.out.println(pz.get(i).getPathId());
		// }
		System.out.println("done");
	}

	private void removeUnwantedIntersections() {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> ents = dao.getAllLocationsForUser("NMMU", null,
				null);
		PathDAO pdao = new PathDAO();
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (AMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			conn.setAutoCommit(false);
			for (int i = 0; i < ents.size(); i++) {
				LocationENT l = ents.get(i);
				for (int j = 0; j < l.getEntrances().size(); j++) {
					if (!l.getEntrances().get(j).isEntranceIntersection()) {
						ArrayList<PathENT> pz = pdao.getAllPathsForOnePoint(l
								.getEntrances().get(j).getEntranceId(), 1);
						if (pz.size() == 0) {
							System.out.println(l.getLocationName() + " "
									+ l.getEntrances().get(j).getEntranceId()
									+ " ");
							try {
								dao.deleteEntrance(l.getEntrances().get(j),
										conn);
							} catch (AMSException e) {
								e.printStackTrace();
							}
						}
						if (pz.size() == 1) {
							System.out.println(l.getLocationName() + " "
									+ l.getEntrances().get(j).getEntranceId()
									+ " ");
							pdao.deletePath(pz.get(0), conn);
							dao.deleteEntrance(l.getEntrances().get(j), conn);
						}

					}
				}
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void addLocationEntranceForBuildings() {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> ents = dao.getAllLocationsForUser("NMMU",
				"3,10", null);
		PathDAO pdao = new PathDAO();
		for (int i = 0; i < ents.size(); i++) {
			EntranceIntersectionENT e = new EntranceIntersectionENT(ents.get(i)
					.getLocationID(), "", ents.get(i).getCoordinates(), true, true,
					true, true, false);
			e = dao.saveEntrance(e, null);
			for (int j = 0; j < ents.get(i).getEntrances().size(); j++) {
				if (!ents.get(i).getEntrances().get(j).isEntranceIntersection())
					continue;
				PathENT p = new PathENT();
				p.setDeparture(new LocationENT(ents.get(i).getEntrances()
						.get(j).getEntranceId()));
				// System.out.println(ents.get(i).getEntrances()
				// .get(j).getEntranceId() + " >>> " + e.getEntranceId());
				p.setDestination(new LocationENT(e.getEntranceId()));
				p.setPathType(9 + "");
				p.setDistance(0);
				p.setWidth(3);
				p.setPathName("Location Entrance");
				pdao.savePath(p, null);
			}
		}
		System.out.println("hi");
	}

	private void findNotConnectedBuildings() {
		LocationDAO dao = new LocationDAO();
		ArrayList<LocationENT> ents = dao.getAllLocationsForUser("NMMU", null,
				null);
		PathDAO pdao = new PathDAO();
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (AMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			conn.setAutoCommit(false);
			for (int i = 0; i < ents.size(); i++) {
				LocationENT l = ents.get(i);
				for (int j = 0; j < l.getEntrances().size(); j++) {
					if (l.getEntrances().get(j).isEntranceIntersection()) {
						ArrayList<PathENT> pz = pdao.getAllPathsForOnePoint(l
								.getEntrances().get(j).getEntranceId(), 1);
						if (pz.size() <= 1) {
							System.out.println(l.getLocationName() + " "
									+ pz.size() + " " + l.getLocationID() + " "
									+ l.getEntrances().get(j).getEntranceId()
									+ " ");
						}
					}
				}
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private static void updateAllDistances() {
		PathDAO pdao = new PathDAO();
		DataManagementLocation daomng = new DataManagementLocation();
		ArrayList<PathENT> paths;
		try {
			System.out.println("1---->" + System.currentTimeMillis());
			paths = daomng.getAllPaths();
			System.out.println("2---->" + System.currentTimeMillis());
			for (int i = 0; i < paths.size(); i++) {
				PathENT p = paths.get(i);
				double dis = PathDAO.calculateDistance(paths.get(i)
						.getDeparture().getEntrances().get(0).getCoordinates(),
						paths.get(i).getDestination().getEntrances().get(0)
								.getCoordinates(), paths.get(i).getPathRoute());
				if (paths.get(i).getDistance() != dis)
					System.out
							.println(p.getPathId() + " before "
									+ paths.get(i).getDistance()
									+ "  >> After: " + dis);
				p.setDistance(dis);
				daomng.updateAPathLength(p);
			}
		} catch (AMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
