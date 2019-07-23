package com.armyPOC.emailService.controller;


import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EmailController
{

   @Autowired
   JmsTemplate jmsTemplate;


   @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
   public String sendEmail() {

      System.out.println("Sending ************************ .");

//      new Email(
//            "info@example.com", "Hello")
      jmsTemplate.convertAndSend(JmsConfig.JMS_TOPIC_MAIL, "send");


      return "Email send success!!!";
   }
}
