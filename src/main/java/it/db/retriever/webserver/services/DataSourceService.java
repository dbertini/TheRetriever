package it.db.retriever.webserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.db.retriever.core.ApplicationContext;

@Path("api/datasources")
public class DataSourceService {
	@GET
	  @Path("datasourceList")
	  @Produces({MediaType.APPLICATION_JSON})
	  public Response getDataSourcesList() {
		  return Response.ok(ApplicationContext.INSTANCE.getDataSources()).build();
	  }
}
