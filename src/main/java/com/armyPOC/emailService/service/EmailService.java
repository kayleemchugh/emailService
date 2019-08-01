package com.armyPOC.emailService.service;

import org.springframework.stereotype.Service;


public interface EmailService
{
    public void sendMimeMessage(String to, String subject, String text);

}
