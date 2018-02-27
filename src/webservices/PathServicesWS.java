package webservices;

import hibernate.config.NMMUMobileDAOManager;
import hibernate.location.LocationDAOInterface;
import hibernate.route.PathDAOInterface;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tools.AMSException;
import common.location.LocationENT;
import common.location.PathENT;
import common.location.PathTypeENT;

@Path("GetPathWS")
public class PathServicesWS {

	@GET
	@Path("/GetPathsForUserAndParent")
	@Produces("application/json")
	public String getPathsForUserAndParent(
			@QueryParam("userName") String userName,
			@QueryParam("parentId") long parentId) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getPathDAO()
					.getRoutesForUserAndParent(userName, parentId));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/GetADirectionFromTo")
	@Produces("application/json")
	public String getADirectionFromTo(
			@QueryParam("clientName") String clientName,
			@QueryParam("from") String from, @QueryParam("to") String to,
			@QueryParam("pathType") int pathType,
			@QueryParam("destinationId") long destinationId,
			@QueryParam("departureId") long departureId) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			LocationENT destENT = new LocationENT();
			if (destinationId <= 0) {
				destENT = getPathDAO().findClosestLocation(to, null, "NMMU");
				destinationId = destENT.getEntrances().get(0).getEntranceId();
			}
			if (departureId <= 0) {
				departureId = getPathDAO()
						.findClosestLocation(from, "", "NMMU").getEntrances()
						.get(0).getEntranceId();
			}
			long destinationIdTMPEntranceID = getLocationDAO()
					.getHiddenLocationEntranceLocationId(destinationId, null);
			long departureIdTMPEntranceID = getLocationDAO()
					.getHiddenLocationEntranceLocationId(departureId, null);
			if (destinationIdTMPEntranceID > 0)
				destinationId = destinationIdTMPEntranceID;
			if (departureIdTMPEntranceID > 0)
				departureId = departureIdTMPEntranceID;
			ArrayList<PathENT> res = getPathDAO().getShortestPath(departureId,
					destinationId, pathType, clientName, pathType);
			if (res.size() == 0)
				getPathDAO().saveTrip(departureId, destinationId, pathType);
			json = mapper.writeValueAsString(res);
			System.out.println(json);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(departureId + " " + json + " " + destinationId);
		return json;
	}

	@GET
	@Path("/UpdatePathWidth")
	@Produces("application/json")
	public String updatePathWidth(@QueryParam("userName") String userName,
			@QueryParam("pathId") long pathId, @QueryParam("width") int width) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PathENT p = getPathDAO().getAPath(new PathENT(pathId), null);
			p.setWidth(width);
			json = mapper.writeValueAsString(getPathDAO().savePath(p, null));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/SavePath")
	@Produces("application/json")
	public String savePath(@QueryParam("fLocationId") long fLocationId,
			@QueryParam("tLocationId") long tLocationId,
			@QueryParam("pathRoute") String pathRoute,
			@QueryParam("pathName") String pathName,
			@QueryParam("description") String description,
			@QueryParam("pathType") String pathType,
			@QueryParam("pathId") long pathId, @QueryParam("width") double width) {
		PathENT ent = new PathENT(new LocationENT(fLocationId),
				new LocationENT(tLocationId), 0, pathType, pathId, pathRoute,
				width, pathName, description);
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(getPathDAO().savePath(ent, null));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/SaveAndConnectAPathToAnExistingPath")
	@Produces("application/json")
	public String saveAndConnectAPathToAnExistingPath(
			@QueryParam("fLocationId") long fLocationId,
			@QueryParam("fLocationGPS") String fLocationGPS,
			@QueryParam("tLocationId") long tLocationId,
			@QueryParam("tLocationGPS") String tLocationGPS,
			@QueryParam("pathRoute") String pathRoute,
			@QueryParam("pathName") String pathName,
			@QueryParam("description") String description,
			@QueryParam("pathType") String pathType,
			@QueryParam("destinationPathId") long destinationPathId,
			@QueryParam("departurePathId") long departurePathId,
			@QueryParam("width") double width) {
		LocationENT from = new LocationENT(fLocationId);
		from.setCoordinates(fLocationGPS);
		LocationENT to = new LocationENT(tLocationId);
		to.setCoordinates(tLocationGPS);
		PathENT ent = new PathENT(from, to, 0, pathType, 0, pathRoute, width,
				pathName, description);
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(getPathDAO().savePath(ent, null));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/CreateAPointOnThePath")
	@Produces("application/json")
	public String createAPointOnThePath(
			@QueryParam("pathId") long pathId,
			@QueryParam("pointGPS") String pointGPS,
			@QueryParam("index") int index,
			@QueryParam("intersectionEntranceParentId") long intersectionEntranceParentId,
			@QueryParam("departureId") long departureId,
			@QueryParam("newPathRoute") String newPathRoute,
			@QueryParam("pathType") String pathType,
			@QueryParam("width") int width,
			@QueryParam("pathName") String pathName,
			@QueryParam("description") String description) {

		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(getPathDAO().createAPointOnPath(
					pathId, pointGPS, index, intersectionEntranceParentId,
					departureId, newPathRoute, description, pathName, width,
					pathType));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@GET
	@Path("/RemoveAPath")
	@Produces("application/json")
	public String removeAPath(@QueryParam("pathId") long pathId) {
		String json = "[]";
		try {
			getPathDAO().deletePath(new PathENT(pathId), null);
		} catch (AMSException e) {
			e.printStackTrace();
		}
		return json;
	}

	private static PathDAOInterface getPathDAO() {
		return NMMUMobileDAOManager.getPathDAOInterface();
	}

	private static LocationDAOInterface getLocationDAO() {
		return NMMUMobileDAOManager.getLocationDAOInterface();
	}
}
