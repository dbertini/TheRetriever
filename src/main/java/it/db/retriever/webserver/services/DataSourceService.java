package it.db.retriever.webserver.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.configuration.entity.DataSource;

@Path("api/datasources")
public class DataSourceService {
	@GET
	@Path("datasourceList")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDataSourcesList() {
		return Response.ok(ApplicationContext.INSTANCE.getDataSources()).build();
	}

	@GET
	@Path("datasourceDetail/{datasourcename}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDataSource(@PathParam("datasourcename") String datasourcename) {
		DataSource ds = null;
		try {
			ds = ApplicationContext.INSTANCE.getDataSource(datasourcename);
		} catch (Exception e) {
			LogManager.getLogger(DataSourceService.class)
					.fatal("Errore durante la ricerca del datasource con nome: " + datasourcename);
			LogManager.getLogger(DataSourceService.class).fatal(e);
		}

		if (ds != null)
			return Response.ok(ds).build();
		else
			return Response.ok("Nessun DataSource trovato per il nome " + datasourcename).build();
	}
}
