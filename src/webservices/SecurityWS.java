package webservices;

import hibernate.config.NMMUMobileDAOManager;
import hibernate.security.SecurityDAOInterface;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("GetSecurityWS")
public class SecurityWS {

	@GET
	@Path("/GetAllRoleCategories")
	@Produces("application/json")
	public String getAllRoleCategories(@QueryParam("filterTxt") String filterTxt) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(getSecurityDAO().getAllRoleCategories(filterTxt));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	private static SecurityDAOInterface getSecurityDAO() {
		return NMMUMobileDAOManager.getSecuirtyDAOInterface();
	}
}
