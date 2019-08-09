package com.armyPOC.emailService.service;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import com.armyPOC.emailService.config.JmsConfig;
//import com.armyPOC.emailService.config.RestClient;
import com.armyPOC.emailService.model.Email;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import io.reactivex.Single;
import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
//import java.util.Scanner;

public class WebsocketListenerService
{

   private HubConnection hubConnection;
   private JmsTemplate jmsTemplate;

   //private static final String URI = "/workflows/2d708852604e434e9513b10ee13849ea/triggers/manual/paths/invoke/1/email?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=6D0BbKXR-LSVsjNRoGy6TwhMQmjzoSovGuaCqY_ycEU";
   //private static final String hubURL = "https://army-poc.service.signalr.net/client/?hub=EmailServiceHub"; // good one
   //private static final String hubURL = "https://army-poc.service.signalr.net:5001/client/?hub=EmailServiceHub"; //bad one
   private static final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJNQWRraW5zLU1CUF8zZWE1NGM0NDAyODY0ODU0YjM1YmU1MzM3OTE0NjQ2MyIsIm5iZiI6MTU2NDY4MjU5NCwiZXhwIjoxNTgxOTYyNTk0LCJpYXQiOjE1NjQ2ODI1OTQsImF1ZCI6Imh0dHBzOi8vYXJteS1wb2Muc2VydmljZS5zaWduYWxyLm5ldC9jbGllbnQvP2h1Yj1FbWFpbFNlcnZpY2VIdWIifQ.ESb7-0kY0Zby327GJH78tDq0iyl_wEaEwwbtbgdyD6Y";
   private static final String hubName = "EmailServiceHub";
   private static final String CONNECTION_STRING = "Endpoint=https://army-poc.service.signalr.net;AccessKey=/FmGhxOruMk9uKf9raGD6sGnFAkH7q/OyDIBan5mcLM=;Version=1.0;";
   private String accessKey;
   private String endpoint;
   private String newToken;

   public WebsocketListenerService() {

   }

   public WebsocketListenerService(JmsTemplate jmsTemplate) {
      this.jmsTemplate = jmsTemplate;

      String[] info = ParseConnectionString(CONNECTION_STRING);
      endpoint = info[0];
      accessKey = info[1];

      String hubURL = endpoint + "/client/?hub=" + hubName;
      //String hubURL = endpoint + ":5001/client/?hub=" + hubName;
      newToken = createJWT("1", "", "", hubURL, accessKey, 100);

      hubConnection = HubConnectionBuilder.create(hubURL)
      .withAccessTokenProvider(Single.defer(() -> {
         // Your logic here.
         return Single.just(newToken);
      })).build();
   }

   public void startClient() {
      hubConnection.on("sendEmail", (email) -> {
            jmsTemplate.convertAndSend(JmsConfig.JMS_TOPIC_MAIL, new Email(
                  email.getEmailAddress(), email.getFirstName(), email.getHtml()));
         }, Email.class);
 
      //This is a blocking call
      hubConnection.start().blockingAwait();;
   }

   //TODO: try catch throw exception
   public String[] ParseConnectionString(String connString){
      String[] properties = connString.split(";");
      if (properties.length > 1){
         String[] returnArr = new String[2];
         Map<String, String> dict = new HashMap<String, String>();
               if (properties.length > 1)
               {
                  for (String property : properties) {
                     String[] pairs = property.split("=", 2);
                     String key = pairs[0].trim();
                     if (dict.containsKey(key)){
                        throw new IllegalArgumentException("Duplicate");
                     }
                     dict.put(key, pairs[1].trim());
                     
                  }
                  if (dict.containsKey("Endpoint") && dict.containsKey("AccessKey"))
                   {
                       returnArr[0] = dict.get("Endpoint");
                       returnArr[1] = dict.get("AccessKey");
                   }
               }
         return returnArr;
      }
      throw new IllegalArgumentException("Connection string missing required properties endpoint and access key");
   }

   private static String createJWT(String id, String issuer, String subject, String audience, String accessKey, long ttlMillis) {

      Header header = Jwts.header();
      header.setType("JWT");

		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
      Date now = new Date(nowMillis);
      Date exp = new Date(nowMillis + 90);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(accessKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

      //if it has been specified, let's add the expiration
      //if (ttlMillis >= 0) {
			//long expMillis = nowMillis + ttlMillis;
			//Date exp = new Date(expMillis);
		//}

      Claims claims = Jwts.claims();
         claims.put("nameid", "MAdkins-MBP_3ea54c4402864854b35be53379146463");

		//Let's set the JWT Claims
      String builder = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setNotBefore(now)
            .setExpiration(exp)
            .setIssuedAt(now)
				.setSubject(null)
				.setIssuer(null)
				.setAudience(audience)
            .signWith(SignatureAlgorithm.HS256, signingKey)
            .compact();

		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder;
	}
}
