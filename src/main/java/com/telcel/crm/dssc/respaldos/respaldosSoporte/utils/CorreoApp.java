package com.telcel.crm.dssc.respaldos.respaldosSoporte.utils;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;

public class CorreoApp {

	public void envia(String asunto, String cuerpo, String destinatarios, String urlPropiedades) {
		try {			
			Properties p = new Properties();
			p.load(new FileReader(urlPropiedades));
			Utilerias utilerias = new Utilerias();
			
			Properties properties = new Properties();
	        properties.put("mail.smtp.host", p.getProperty("host"));
	        properties.put("mail.smtp.port", p.getProperty("port"));
	        properties.put("mail.smtp.auth", p.getProperty("auth"));
	        properties.put("mail.smtp.ssl.trust", p.getProperty("trust"));
			
			List<String> listDestinantaios = utilerias.getDestinatarios(destinatarios);
			
			Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(p.getProperty("username"), p.getProperty("password"));
	            }
	        });
			
			Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(p.getProperty("username")));
            message.setSubject(asunto);
            
            for(String destinatario : listDestinantaios) {
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            }
            message.setContent(cuerpo, "text/html");
            Transport.send(message);
            System.out.println("Correo enviado exitosamente.");
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
