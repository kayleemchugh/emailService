package com.armyPOC.emailService.model;

public class Email
{
   private String emailAddress;
   private String html;
   private String firstName;

   public Email() {

   }

   
   public Email(String emailAddress, String firstName, String html) {
      this.setEmailAddress(emailAddress);
      this.setFirstName(firstName);
      this.setHtml(html);
   }

   public String getEmailAddress() {
      return emailAddress;
   }

   public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getHtml() {
      return html;
   }

   public void setHtml(String html) {
      this.html = html;
   }

}
