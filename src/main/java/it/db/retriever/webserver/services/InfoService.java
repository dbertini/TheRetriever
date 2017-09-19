package it.db.retriever.webserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.utils.Version;

@Path("api/info")
public class InfoService {
  @GET
  @Path("version")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getVersion() {
	  return Response.ok(Version.version).build();
  }
  
  
  @GET
  @Path("configuration")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getConfiguration() {
	  return Response.ok(ApplicationContext.INSTANCE.getConfiguration()).build();
  }
}