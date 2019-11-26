package it.db.retriever.webserver.services11;

import it.db.retriever.core.ApplicationContext;
import it.smartrest.annotations.GET;
import it.smartrest.handlers.RestHandler;
import it.smartrest.responses.JSONReponse;

public class InfoServiceConfiguration extends RestHandler {
	@GET
	public JSONReponse getConfiguration() {
		return new JSONReponse(ApplicationContext.INSTANCE.getConfiguration());
	}
}
