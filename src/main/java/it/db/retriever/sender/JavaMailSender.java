package it.db.retriever.sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;

import it.db.retriever.core.ApplicationContext;

public class JavaMailSender implements ReportSenderInterface {

	@Override
	public void sendMailWithAttachments(String aSubject, String aHTMLMessage, String aRecipients, String aCcList,
			String aCcnList, String... aAttachmentsFile)  throws Exception {

		String username = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.username");
		String password = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.password");

		String to = aRecipients;
		String from = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.username");
		String host = ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.smtp.server");
		//String filename = aNameFileAttachment;

		String msgText1 = aHTMLMessage;
		String subject = aSubject;

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", true); // added this line
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", ApplicationContext.INSTANCE.getConfiguration().getProperty("sender.smtp.port"));
		props.put("mail.smtp.auth", true);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setRecipients(Message.RecipientType.CC, aCcList);
			msg.setRecipients(Message.RecipientType.BCC, aCcnList);

			msg.setSubject(subject);

			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(msgText1, "text/html");

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();

			mp.addBodyPart(mbp1);

			if (aAttachmentsFile != null && aAttachmentsFile.length > 0) {
				List<String> attachemente = new ArrayList<String>();
				Collections.addAll(attachemente, aAttachmentsFile);
				
				attachemente.stream().forEachOrdered(att -> {
					// create the second message part
					MimeBodyPart mbp2 = new MimeBodyPart();
					// attach the file to the message
					try {
						mbp2.attachFile(att);
						mp.addBodyPart(mbp2);
					} catch (IOException | MessagingException e) {
						LogManager.getLogger(JavaMailSender.class).error("Errore durante il processo di allegamento del file: " + att);
						LogManager.getLogger(JavaMailSender.class).error(e);
					}
				});
			}

			// add the Multipart to the message
			msg.setContent(mp);

			// set the Date: header
			msg.setSentDate(new Date());

			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());

		} catch (MessagingException mex) {
			LogManager.getLogger(JavaMailSender.class).error(mex);
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				LogManager.getLogger(JavaMailSender.class).error(ex);
			}
		}
	}
}
