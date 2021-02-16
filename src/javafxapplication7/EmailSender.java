/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author el_le
 */
public class EmailSender {
    
    
    public void SendMailBySite(String recepient) {
        
        System.out.println("[+] EmailSender.SendMailBySite() " + recepient);
        String host = "smtp.mail.com";
        final String user = "arpsoofdetector@cyberservices.com";//change accordingly  
        final String password = "w^L6H0#UXtgo";//change accordingly  

        //Get the session object  
        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        //Compose the message  
        
            try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Your device may be at risk!");
            message.setText("This is simple program of sending email using JavaMail API");

            //send the message  
            Transport.send(message);

            System.out.println("message sent successfully...");

        }
        catch (MessagingException e

        
            ) {
                e.printStackTrace();
        }
    }
}
