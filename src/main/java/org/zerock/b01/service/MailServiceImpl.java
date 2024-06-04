package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.zerock.b01.dto.MailDTO;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "gohwangbong@gmail.com";

    @Override
    public void createMail(MailDTO mailDTO) {
        log.info("--------------------create mail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getReceiver());
        message.setFrom(senderEmail);
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getContent());

        log.info("mailDTO : " + mailDTO);
        log.info("message : " + message);

        javaMailSender.send(message);
    }
}
