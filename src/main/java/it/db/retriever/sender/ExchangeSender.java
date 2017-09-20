package it.db.retriever.sender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.ApplicationContext;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.autodiscover.exception.AutodiscoverLocalException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class ExchangeSender implements ReportSenderInterface {
	
	@Override
	public void sendMailWithAttachments(String aSubject, String aHTMLMessage, String aRecipients, String aCcList, String aCcnList, String... aAttachmentsFile) throws Exception {
		String lsUsername = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.username");
		String lsPassword = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.password");
		String lsMailBox = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.mailbox");
		String lsVersion = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.exchange.version");

		ExchangeService service = new ExchangeService(ExchangeVersion.valueOf(lsVersion));
		
		LogManager.getLogger(ExchangeSender.class).trace("Versione Exchange: " + ExchangeVersion.valueOf(lsVersion).toString());

		// utente e password per accedere al servizio
		LogManager.getLogger(ExchangeSender.class).info("Utente per accesso a Exchange: " + lsUsername);
		ExchangeCredentials credentials = new WebCredentials(lsUsername, lsPassword);

		service.setCredentials(credentials);
		// tramite utente e password recupero dell'indirizzo del server di Exchange
		LogManager.getLogger(ExchangeSender.class).info("Mailbox da utilizzare: " + lsMailBox);
		service.autodiscoverUrl(lsMailBox, new IAutodiscoverRedirectionUrl() {
			public boolean autodiscoverRedirectionUrlValidationCallback(String redirectionUrl)
					throws AutodiscoverLocalException {
				return true;
			}
		});

		// inizializzazione del messaggio
		EmailMessage msg = new EmailMessage(service);
		// scrivo l'oggetto della mail
		msg.setSubject(aSubject);
		// scrivo il body in HTML della mail
		msg.setBody(MessageBody.getMessageBodyFromText(aHTMLMessage));

		// si aggiungono i destinatari
		ArrayList<String> recipientsList = new ArrayList<String>();
		Collections.addAll(recipientsList, aRecipients.split(";"));
		recipientsList.stream().forEachOrdered(rec -> {
			try {
				msg.getToRecipients().add(rec);
			} catch (ServiceLocalException e) {
				throw new IllegalArgumentException(e);
			}
		});

		// se presenti si aggiungono i cc
		if (aCcList != null && !aCcList.trim().equalsIgnoreCase("")) {
			ArrayList<String> ccList = new ArrayList<String>();
			Collections.addAll(ccList, aCcList.split(";"));
			ccList.stream().forEachOrdered(ccr -> {
				try {
					msg.getCcRecipients().add(ccr);
				} catch (ServiceLocalException e) {
					throw new IllegalArgumentException(e);
				}
			});
		}

		// se presenti si aggiungono i ccn
		if (aCcnList != null && !aCcnList.trim().equalsIgnoreCase("")) {
			ArrayList<String> ccnList = new ArrayList<String>();
			Collections.addAll(ccnList, aCcnList.split(";"));

			ccnList.stream().forEachOrdered(ccn -> {
				try {
					msg.getBccRecipients().add(ccn);
				} catch (ServiceLocalException e) {
					throw new IllegalArgumentException(e);
				}
			});
		}

		if (aAttachmentsFile != null && aAttachmentsFile.length > 0) {
			// Aggiunga dei file da allegare se presenti
			List<String> attachemente = new ArrayList<String>();
			Collections.addAll(attachemente, aAttachmentsFile);

			attachemente.stream().forEachOrdered(att -> {
				try {
					msg.getAttachments().addFileAttachment(att);
				} catch (ServiceLocalException e) {
					throw new IllegalArgumentException(e);
				}
			});
		}

		// invio del messagio
		LogManager.getLogger(ExchangeSender.class).info("Invio del messaggio....");
		msg.send();
		LogManager.getLogger(ExchangeSender.class).info("....Messaggio inviato correttamente");
	}
}