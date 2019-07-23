package com.armyPOC.emailService.messaging;


import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.model.Email;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver
{

   @JmsListener(destination = JmsConfig.JMS_TOPIC_MAIL)
   public void receiveMessage(Email email) {
      System.out.println("Received " + email.getBody() + " email");
      //TODO - send email to address, send back confirmation message
   }
}
