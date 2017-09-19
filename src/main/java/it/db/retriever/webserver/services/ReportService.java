package it.db.retriever.webserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.db.retriever.core.ApplicationContext;

@Path("api/reports")
public class ReportService {
  @GET
  @Path("reportList")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getReportList() {
	  return Response.ok(ApplicationContext.INSTANCE.getReports()).build();
  }
}