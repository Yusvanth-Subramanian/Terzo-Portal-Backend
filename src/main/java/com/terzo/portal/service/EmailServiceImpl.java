package com.terzo.portal.service;

import com.terzo.portal.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean send(EmailDTO emailDTO) {
        boolean isSent=false;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailDTO.getTo());
        mailMessage.setSubject(emailDTO.getSubject());
        mailMessage.setText(emailDTO.getBody());

        try {
            mailSender.send(mailMessage);
            isSent=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isSent;
    }
}
