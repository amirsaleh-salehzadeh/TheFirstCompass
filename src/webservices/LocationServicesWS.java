package webservices;

import hibernate.config.NMMUMobileDAOManager;
import hibernate.location.LocationDAOInterface;
import hibernate.route.PathDAOInterface;

import java.io.IOException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import common.location.EntranceIntersectionENT;
import common.location.LocationENT;
import common.location.LocationTypeENT;
import common.location.PathENT;
import tools.AMSException;

@Path("GetLocationWS")
public class LocationServicesWS {

	@GET
	@Path("/CreateTFCEntrance")
	@Produces("application/json")
	public String createTFCEntrance(
			@QueryParam("locationName") String locationName,
			@QueryParam("coordinate") String coordinate,
			@QueryParam("username") String userName,
			@QueryParam("parentId") long parentId,
			@QueryParam("entranceId") long entranceId,
			@QueryParam("isEntrance") boolean isEntrance) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			EntranceIntersectionENT ent = new EntranceIntersectionENT(parentId,
					locationName, coordinate, isEntrance, true, false, true,
					false);
			ent.setEntranceId(entranceId);
			json = mapper.writeValueAsString(getLocationDAO().saveEntrance(ent,
					null));
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
	@Path("/CreateTFCLevels")
	@Produces("application/json")
	public String createTFCLevels(
			@QueryParam("locationName") String locationName,
			@QueryParam("locationType") String parentLocationIds,
			@QueryParam("username") String userName,
			@QueryParam("parentId") long parentId) {
		ObjectMapper mapper = new ObjectMapper();

		String json = "";

		try {
			LocationENT ent = new LocationENT();
			if (locationName.equalsIgnoreCase("Level"))
				ent.setLocationType(new LocationTypeENT(4));
			ent.setBoundary(null);
			ent.setLocationName(locationName);
			ent.setParentId(parentId);
			ent.setIcon(null);
			ent.setPlan(null);
			ent.setDescription(null);
			json = mapper.writeValueAsString(getLocationDAO()
					.saveUpdateLocation(ent, null));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AMSException e) {
			e.printStackTrace();
		}

		return json;
	}

	@GET
	@Path("/GetAllLocationsForUser")
	@Produces("application/json")
	public String getAllLocationsForUser(
			@QueryParam("userName") String userName,
			@QueryParam("locationTypeId") String locationTypeIds,
			@QueryParam("parentLocationId") String parentLocationIds) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			if (parentLocationIds.equalsIgnoreCase("0"))
				parentLocationIds = null;
			json = mapper.writeValueAsString(getLocationDAO()
					.getAllLocationsForUser(userName, locationTypeIds,
							parentLocationIds));
			System.out.println(json);
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
	@Path("/GetChildrenOfALocation")
	@Produces("application/json")
	public String getChildrenOfALocation(
			@QueryParam("userName") String userName,
			@QueryParam("locationTypeId") String locationTypeIds,
			@QueryParam("parentLocationId") String parentLocationIds) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			if (parentLocationIds.equalsIgnoreCase("0"))
				parentLocationIds = null;
			json = mapper.writeValueAsString(getLocationDAO()
					.getChildrenOfAlocationUser(userName, locationTypeIds,
							parentLocationIds));
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
	@Path("/GetLocationWithChildren")
	@Produces("application/json")
	public String getLocationWithChildren(
			@QueryParam("locationID") long locationID) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getLocationDAO()
					.getLocationWithChildren(new LocationENT(locationID)));
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
	@Path("/SearchForALocation")
	@Produces("application/json")
	public String searchForALocation(@QueryParam("username") String username,
			@QueryParam("locationType") String locationType,
			@QueryParam("locationName") String locationName) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			LocationENT search = new LocationENT();
			search.setLocationName(locationName);
			search.setLocationType(new LocationTypeENT(0, locationType));
			search.setUserName(username);
			search.setLocationID(360);
			json = mapper.writeValueAsString(getLocationDAO()
					.getLocationWithChildren(search));
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
	@Path("/GetAllLocationTypes")
	@Produces("application/json")
	public String getAllLocationTypes() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getLocationDAO()
					.getAllLocationTypeChildren(new LocationTypeENT(1)));
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
	@Path("/GetAllPathTypes")
	@Produces("application/json")
	public String getAllPathTypes() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getPathDAO().getAllPathTypes());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@POST
	@Path("/SaveUpdateLocation")
	@Produces("application/json")
	@Consumes({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_FORM_URLENCODED })
	public String saveUpdateLocation(@FormParam("icon") String icon,
			@FormParam("locationId") long locationId,
			@FormParam("locationTypeId") String locationTypeId,
			@FormParam("locationName") String locationName,
			@FormParam("userName") String userName,
			@FormParam("coordinate") String coordinate,
			@FormParam("description") String description,
			@FormParam("boundary") String boundary,
			@FormParam("plan") String plan, @FormParam("parentId") long parentId) {
		LocationENT ent = new LocationENT();
		ent.setUserName(userName);
		ent.setLocationName(locationName);
		ent.setCoordinates(coordinate);
		ent.setLocationID(locationId);
		ent.setLocationType(new LocationTypeENT(Integer
				.parseInt(locationTypeId)));
		ent.setParentId(parentId);
		ent.setDescription(description);
		ent.setBoundary(boundary);
		ent.setPlan(plan);
		ent.setIcon(icon);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getLocationDAO()
					.saveUpdateLocation(ent, null));
		} catch (AMSException e) {
			e.printStackTrace();
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
	@Path("/GetAPath")
	@Produces("application/json")
	public String getAPath(@QueryParam("pathId") long pathId) {
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(getPathDAO().getAPath(
					new PathENT(pathId), null));
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
	@Path("/GetALocation")
	@Produces("application/json")
	public String getALocation(@QueryParam("locationId") long locationId) {
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(getLocationDAO().getLocationENT(
					new LocationENT(locationId), null));
			System.out.println(json);
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
	@Path("/GetALocationAR")
	@Produces("application/json")
	public String getALocationAR(@QueryParam("locationId") long locationId) {
		String json = "[]";
		LocationENT l = getLocationDAO().getLocationENT(
				new LocationENT(locationId), null);
		for (int i = 0; i < l.childrenENT.size(); i++) {
			System.out.println(l.childrenENT.get(i).getLocationName());
		}
		json = "[{\"latitude\":"
				+ Double.parseDouble(l.getCoordinates().split(",")[0])
				+ ",\"longitude\":"
				+ Double.parseDouble(l.getCoordinates().split(",")[1])
				+ ", \"label\":\"" + l.getLocationName() + "\"}]";
		// System.out.println(json);
		return json;
	}

	@GET
	@Path("/GetALocationChildrenAR")
	@Produces("application/json")
	public String getALocationChildrenAR(
			@QueryParam("locationId") long locationId) {
		String json = "[]";
		ArrayList<LocationENT> l = getLocationDAO().getChildrenOfAlocationUser(
				"admin", 2 + "", "");
		json = "[";
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i).getLocationName());
			json += "{\"latitude\":"
					+ Double.parseDouble(l.get(i).getCoordinates().split(",")[0])
					+ ",\"longitude\":"
					+ Double.parseDouble(l.get(i).getCoordinates().split(",")[1])
					+ ", \"label\":\"" + l.get(i).getLocationName() + "\"}";
			if (i < l.size() - 1)
				json += ",";
		}

		json += "]";
		System.out.println(json);
		return json;
	}

	@GET
	@Path("/RemoveALocation")
	@Produces("application/json")
	public String removeALocation(@QueryParam("locationId") long locationId) {
		String json = "[]";
		try {
			if (getLocationDAO().deleteLocation(new LocationENT(locationId))) {
				json = "{\"errorMSG\": null}";
			} else {
				json = "{\"errorMSG\": \"Problem while removing the location\"}";
			}
		} catch (AMSException e) {
			e.printStackTrace();
			return "{\"errorMSG\": \"Please remove the path first\"}";
		}
		return json;
	}

	@GET
	@Path("/RemoveEntrance")
	@Produces("application/json")
	public String removeAnEntrance(
			@QueryParam("entranceIntersectionId") long entranceIntersectionId) {
		String json = "[]";
		try {
			if (getLocationDAO().deleteEntrance(
					new EntranceIntersectionENT(entranceIntersectionId), null)) {
				json = "{\"errorMSG\": null}";
			} else {
				json = "{\"errorMSG\": \"Problem while removing the location\"}";
			}
		} catch (AMSException e) {
			e.printStackTrace();
			return "{\"errorMSG\": \"Please remove the path first\"}";
		}
		return json;
	}

	@GET
	@Path("/GetBarcodeForLocation")
	@Produces("application/json")
	public String getBarcodeForLocation(
			@QueryParam("locationId") long locationId) {
		return getLocationDAO().getQRCodeForLocationENT(locationId);
	}

	@GET
	@Path("/GetBarcodeInTripInfo")
	@Produces("application/json")
	public String getBarcodeinTripInfo(@QueryParam("barcodeId") long barcodeId,
			@QueryParam("destinationId") long destinationId,
			@QueryParam("pathType") int pathType) {
		String json = "[]";
		// ObjectMapper mapper = new ObjectMapper();
		// // try {
		// ArrayList<PathENT> shortestPathToDest = getLocationDAO()
		// .getShortestPath(barcodeId, destinationId, pathType);
		// ArrayList<PathENT> allPathsforADest = getLocationDAO()
		// .getAllPathsForOnePoint(barcodeId, pathType);

		// } catch (JsonGenerationException e) {
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return json;
	}

	// @GET
	// @Path("/StartTrip")
	// @Produces("application/json")
	// public String startTrip(@QueryParam("from") long from,
	// @QueryParam("to") long to) {
	// String json = "[]";
	// json = "[{\"tripId\" : \"" + getLocationDAO().saveTrip(from, to)
	// + "\"}]";
	// return json;
	// }

	// @GET
	// @Path("/RemoveTrip")
	// @Produces("application/json")
	// public String removeTrip(@QueryParam("tripId") long tripId) {
	// String json = "[]";
	// getLocationDAO().deleteTrip(tripId);
	// return json;
	// }

	private static LocationDAOInterface getLocationDAO() {
		return NMMUMobileDAOManager.getLocationDAOInterface();
	}

	private static PathDAOInterface getPathDAO() {
		return NMMUMobileDAOManager.getPathDAOInterface();
	}
}
