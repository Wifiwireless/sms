package com.wireless.email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.wifiwireless.model.CustomerDetails;
import com.wireless.utility.NexmoServices;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Mail {

	public static void email(String emailid, String subject, Map<String, String> rootMap, String temp)
			throws IOException, TemplateException {

		Properties props = new Properties();

		props.put("mail.smtp.host", "127.0.0.1");
		props.put("mail.smtp.port", "25");

		// * Testing---
		/*
		 * props.put("mail.smtp.auth", "true");
		 * props.put("mail.smtp.starttls.enable", "true");
		 * props.put("mail.smtp.host", "smtp.gmail.com");
		 * props.put("mail.smtp.port", "587");
		 */
		/*
		 * javax.mail.Session session = Session.getInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { System.out.println(authUsername +
		 * authPAss); return new PasswordAuthentication(authUsername, authPAss);
		 * } });
		 */
		/*
		 * props.put("mail.smtp.host", "smtp.gmail.com");
		 * props.put("mail.smtp.socketFactory.port", "465");
		 * props.put("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory"); props.put("mail.smtp.auth",
		 * "true"); props.put("mail.smtp.port", "465");
		 * 
		 * Session session = Session.getDefaultInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new
		 * PasswordAuthentication(authUsername,authPAss); } });
		 */
		Session session = Session.getDefaultInstance(props);

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress("info@utalkwifi.com", "Utalkwifi App support"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailid));
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("kirti.mandwade@gmail.com"));

			message.setSubject(subject);

			BodyPart bodypart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodypart);
			message.setContent(multipart);
			Configuration configuration = new Configuration();
			configuration.setTemplateLoader(new ClassTemplateLoader(NexmoServices.class, "/"));

			Template template = configuration.getTemplate(temp);

			Writer out = new StringWriter();
			template.process(rootMap, out);

			bodypart.setContent(out.toString(), "text/html");

			Transport.send(message);
			System.out.println("email sent to " + emailid);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static Boolean generateVerificationEmail(CustomerDetails customerDetails, String msisdn,boolean isNumberReady) {
		String subject = "UtalkWifi Application Credentials";
		Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("username", customerDetails.getExtension());
		rootMap.put("password", customerDetails.getSecret());
		
		if(isNumberReady){
			rootMap.put("number", msisdn);
		}else{
			rootMap.put("number", "Due to technical problem we will asign you number 1 hour later.");
		}
		
		rootMap.put("date", "" + new Date());
		try {
			Mail.email(customerDetails.getEmail(), subject, rootMap, "email.ftl");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
