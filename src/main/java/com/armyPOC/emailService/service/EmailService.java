package com.armyPOC.emailService.service;


public interface EmailService
{
    public void sendMimeMessage(String to, String subject, String text);

}
