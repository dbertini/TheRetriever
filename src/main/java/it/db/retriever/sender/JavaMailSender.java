package it.db.retriever.sender;

import java.io.IOException;
import java.util.Date;
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

public class JavaMailSender implements ReportSenderInterface {

	@Override
	public void sendMailWithAttachments(String aSubject, String aHTMLMessage, String aRecipients, String aCcList,
			String aCcnList, String aNameFileAttachment) {

		String username = "xxxxxxxxxxx@gmail.com";
		String password = "password";

		String to = aRecipients;
		String from = "the.retriever@gmail.com";
		String host = "smtp.gmail.com";
		String filename = aNameFileAttachment;

		String msgText1 = aHTMLMessage;
		String subject = aSubject;

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", true); // added this line
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", true);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		// Session session = Session.getInstance(props, null);

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
			if (filename != null) {
				// create the second message part
				MimeBodyPart mbp2 = new MimeBodyPart();

				// attach the file to the message
				mbp2.attachFile(filename);
				mp.addBodyPart(mbp2);
			}

			// add the Multipart to the message
			msg.setContent(mp);

			// set the Date: header
			msg.setSentDate(new Date());

			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());

		} catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
}
