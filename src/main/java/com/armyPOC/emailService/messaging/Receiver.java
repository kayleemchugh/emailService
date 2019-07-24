package com.armyPOC.emailService.messaging;


import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.model.Email;
import com.armyPOC.emailService.service.EmailServiceImpl;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver
{

   private EmailServiceImpl emailService;

   public Receiver(EmailServiceImpl emailServiceImp) {
      emailService = emailServiceImp;
   }

   @JmsListener(destination = JmsConfig.JMS_TOPIC_MAIL)
   public void receiveMessage(Email email) {
      System.out.println("Received " + email.getBody() + " email");
      emailService.sendSimpleMessage(email.getTo(), "subject", email.getBody());
   }
}
