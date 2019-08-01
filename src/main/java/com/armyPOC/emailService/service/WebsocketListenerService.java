package com.armyPOC.emailService.service;

import com.armyPOC.emailService.config.JmsConfig;
import com.armyPOC.emailService.config.RestClient;
import com.armyPOC.emailService.model.Email;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

public class WebsocketListenerService
{

   private HubConnection hubConnection;
   private JmsTemplate jmsTemplate;

   //private static final String URI = "/workflows/2d708852604e434e9513b10ee13849ea/triggers/manual/paths/invoke/1/email?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=6D0BbKXR-LSVsjNRoGy6TwhMQmjzoSovGuaCqY_ycEU";
   private static final String hubURL = "https://army-poc.service.signalr.net/client/?hub=EmailServiceHub";
   private static final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJNQWRraW5zLU1CUF8zZWE1NGM0NDAyODY0ODU0YjM1YmU1MzM3OTE0NjQ2MyIsIm5iZiI6MTU2NDY4MjU5NCwiZXhwIjoxNTgxOTYyNTk0LCJpYXQiOjE1NjQ2ODI1OTQsImF1ZCI6Imh0dHBzOi8vYXJteS1wb2Muc2VydmljZS5zaWduYWxyLm5ldC9jbGllbnQvP2h1Yj1FbWFpbFNlcnZpY2VIdWIifQ.ESb7-0kY0Zby327GJH78tDq0iyl_wEaEwwbtbgdyD6Y";
   //private static final String token = "yfygeUdyrJQY+jx4i0eNS1UjmbY/E77pYXt+A6aQ3vo=";

   public WebsocketListenerService() {

     

   

   }

   public WebsocketListenerService(JmsTemplate jmsTemplate) {
      this.jmsTemplate = jmsTemplate;

      hubConnection = HubConnectionBuilder.create(hubURL)
      .withAccessTokenProvider(Single.defer(() -> {
         // Your logic here.
         return Single.just(token);
      })).build();
   }

   public void startClient() {
      


      hubConnection.on("sendEmail", (email) -> {
            jmsTemplate.convertAndSend(JmsConfig.JMS_TOPIC_MAIL, new Email(
                  email.getEmailAddress(), email.getFirstName(), email.getHtml()));
         }, Email.class);
 
      //jmsTemplate.convertAndSend(JmsConfig.JMS_TOPIC_MAIL, new Email(
        // "chatwitherica@gmail.com", "test", "test"));
      //This is a blocking call
      hubConnection.start().blockingAwait();;
   }
}
