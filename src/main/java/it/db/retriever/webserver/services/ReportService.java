package it.db.retriever.webserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.configuration.entity.Report;

@Path("api/reports")
public class ReportService {
  @GET
  @Path("reportList")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getReportList() {
	  return Response.ok(ApplicationContext.INSTANCE.getReports()).build();
  }
  
  @GET
  @Path("reportDetail/{reportname}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getReport(@PathParam("reportname") String reportname) {
	  
	  Report rep[] = {null};
	  ApplicationContext.INSTANCE.getReports().forEach(report -> {
		  if( report.getName().trim().equalsIgnoreCase(reportname) ) {
			  rep[0] = report;
		  }
	  });
	  
	  if(rep[0] != null)
		  return Response.ok(rep[0]).build();
	  else
		  return Response.ok("Nessun report trovato per il nome " + reportname).build();
	  
  }
}