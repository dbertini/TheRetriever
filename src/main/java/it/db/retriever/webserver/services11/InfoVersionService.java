package it.db.retriever.webserver.services11;

import it.db.retriever.utils.Version;
import it.smartrest.annotations.GET;
import it.smartrest.handlers.RestHandler;
import it.smartrest.responses.JSONReponse;

public class InfoVersionService extends RestHandler {
	@GET
	public JSONReponse getVersion() {

		Version ver = new Version();
		JSONReponse r = new JSONReponse(ver);

		return r;
	}
}
