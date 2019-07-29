package com.armyPOC.emailService.config;

import com.armyPOC.emailService.model.Email;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestClient
{

   public static final String SERVER = "https://prod-80.westus.logic.azure.com";
   private HttpStatus status;
   private RestTemplate restTemplate;
   private HttpHeaders httpHeaders;

   public RestClient() {
      restTemplate = new RestTemplate();
      httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);

   }

   public String get(URI uri) {
      HttpEntity<String> requestEntity = new HttpEntity<String>("", httpHeaders);

      ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
      this.setStatus(responseEntity.getStatusCode());
      return responseEntity.getBody();
   }

   public void post(URI uri, Email email) {
      ResponseEntity<Email> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(email, httpHeaders),  new ParameterizedTypeReference<Email>(){});
      this.setStatus(responseEntity.getStatusCode());
   }

   public HttpStatus getStatus() {
      return status;
   }

   private void setStatus(HttpStatus status) {
      this.status = status;
   }
}
