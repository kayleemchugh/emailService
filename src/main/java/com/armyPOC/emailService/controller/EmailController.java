package com.armyPOC.emailService.controller;


import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EmailController
{

   @Autowired
   JmsTemplate jmsTemplate;


/*@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
   public String sendEmail(@RequestBody Email email) {

      System.out.println("Sending ************************ .");

      jmsTemplate.convertAndSend(JmsConfig.JMS_TOPIC_MAIL, new Email(
            email.getEmailAddress(), email.getFirstName(), email.getHtml()));

      return "Email send success!!!";
   }*/
}
