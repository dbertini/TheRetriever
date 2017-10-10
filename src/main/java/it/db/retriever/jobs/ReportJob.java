package it.db.retriever.jobs;

import it.db.retriever.core.configuration.entity.Report;
import it.db.retriever.reportutils.ReportUtil;
import it.db.retriever.utils.StandardParameter;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReportJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        try {
            Report report = (Report) context.getMergedJobDataMap().get(StandardParameter.REPORT);
            LogManager.getLogger(ReportJob.class).info("Inizio esecuzione schedulata del job: " +
                    report.getName());
            ReportUtil.runReport(report);
        } catch (Exception e) {
            LogManager.getLogger(ReportJob.class).error(e);
            throw new JobExecutionException(e);
        }

    }

}
