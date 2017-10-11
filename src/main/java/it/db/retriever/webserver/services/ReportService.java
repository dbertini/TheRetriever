package it.db.retriever.webserver.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.db.retriever.core.ApplicationContext;
import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.reportutils.ReportUtil;
import it.db.retriever.utils.StandardParameter;

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
            if (report.getName().trim().equalsIgnoreCase(reportname)) {
                rep[0] = report;
            }
        });

        if (rep[0] != null)
            return Response.ok(rep[0]).build();
        else
            return Response.ok("Nessun report trovato per il nome " + reportname).build();

    }

    @GET
    @Path("runReport/{reportname}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response runReport(@PathParam("reportname") String reportname) {
        System.out.println("Report name selezionato: " + reportname);
        try {
            Report report = ApplicationContext.INSTANCE.getReport(reportname);
            ReportUtil.runReport(report);
            return Response.ok().build();
        }catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Status.NOT_FOUND).build();
        }
    }



    @POST
    @Path("editreport")
    @Produces({MediaType.APPLICATION_JSON})
    public Response editReport(@FormParam("reportname") String aReportName) {

        System.out.println("Nome del report: " + aReportName);

        if (ApplicationContext.INSTANCE.isReportPresent(aReportName)) {

            //ho trovato il report e devo aggiornare il file

            //come prima cosa faccio una copia della vecchia versione
            String lFileName = ApplicationContext.INSTANCE.getReport(aReportName).getFilename();

            System.out.println("Nome del file trovato: " + lFileName);

            Date adesso = new Date();

            try {
                Files.copy(Paths.get(StandardParameter.REPORTS_PATH + lFileName),
                           Paths.get(StandardParameter.REPORTS_PATH + lFileName + "." + adesso.getTime()),
                            StandardCopyOption.COPY_ATTRIBUTES,
                            StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // instazio il marshaller con il tipo di oggetto della classe
            JAXBContext jaxbContext = null;
            Unmarshaller jaxbUnmarshaller = null;

            try {
                jaxbContext = JAXBContext.newInstance(Report.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                // converzione da XML ad oggetto DataSource
                Report report = (Report) jaxbUnmarshaller.unmarshal(new File(StandardParameter.REPORTS_PATH + lFileName));

                report.setCcnlist("mario.rossi@email.it");


                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                jaxbMarshaller.marshal(report, new File(StandardParameter.REPORTS_PATH + lFileName));

            } catch (JAXBException e) {
                e.printStackTrace();
            }

            // rimozione della schedulazione del report
            /*try {
                ApplicationContext.INSTANCE.getScheduler()
                        .deleteJob(jobKey(aReportName, StandardParameter.REPORT_GROUP));


            } catch (SchedulerException e) {
                e.printStackTrace();
            }*/

            return Response.ok("OK").build();
        } else {

            return Response.status(Status.NOT_FOUND).build();
        }

    }

}