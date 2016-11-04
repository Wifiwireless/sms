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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wifiwireless.model.CustomerDetails;
import com.wireless.utility.NexmoServices;
import com.wireless.utility.Schedulars;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Mail {

	private static final Logger log = LoggerFactory.getLogger(Mail.class);
	
	public static void email(String emailid, String subject, Map<String, String> rootMap, String temp)
			throws IOException, TemplateException {

		Properties props = new Properties();

		props.put("mail.smtp.host", "127.0.0.1");
		props.put("mail.smtp.port", "25");

		
		Session session = Session.getDefaultInstance(props);

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress("info@utalkwifi.com", "Utalkwifi App support"));
			
			if("balanceMail".equals(emailid)){
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("notiont@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("g.curcio@wifiwirelessinc.com"));
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("kirti.mandwade@gmail.com"));
			}else{
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailid));
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("kirti.mandwade@gmail.com"));
			}
			
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
			
			log.info("email sent to " + emailid);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	public static Boolean generateVerificationEmail(CustomerDetails customerDetails, String msisdn,boolean isNumberReady) {
		String subject = "UtalkWifi Application Credentials";
		Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("username", customerDetails.getExtension());
		rootMap.put("password", customerDetails.getSecret());
		log.info("isNumberReady"+isNumberReady);
		if(isNumberReady){
			log.info("MSISDN in email:"+msisdn);
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
