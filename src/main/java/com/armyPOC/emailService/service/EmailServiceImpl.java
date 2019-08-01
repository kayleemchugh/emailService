package com.armyPOC.emailService.service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

   private JavaMailSender emailSender;



   public EmailServiceImpl(JavaMailSender javaMailSender) {
      this.emailSender = javaMailSender;
   }


   public void sendMimeMessage(
         String emailAddress, String subject, String html) {
      try {
         MimeMessage message = emailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
         message.setContent(html, "text/html");
         helper.setTo(emailAddress);
         helper.setSubject(subject);
         emailSender.send(message);
      } catch (MessagingException ex) {
         Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
      }

      /*SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(emailAddress);
      message.setSubject(subject);
      message.setText(html);
      emailSender.send(message);*/
   }


}

