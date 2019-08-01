package com.armyPOC.emailService.messaging;


import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.model.Email;
import com.armyPOC.emailService.service.EmailService;
import com.armyPOC.emailService.service.EmailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver
{
   @Autowired
   private EmailService emailService;

   public Receiver(EmailServiceImpl emailServiceImp) {
      emailService = emailServiceImp;
   }

   @JmsListener(destination = JmsConfig.JMS_TOPIC_MAIL)
   public void receiveMessage(Email email) {
      System.out.println("Received " + email.getHtml() + " email");
      emailService.sendMimeMessage(email.getEmailAddress(), "Hi " + email.getFirstName(), email.getHtml());
   }
}
