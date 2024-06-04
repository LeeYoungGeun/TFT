package org.zerock.b01.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Log4j2
public class MailConfig {

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");	//네이버의 경우 smtp.naver.com
        javaMailSender.setUsername("gorea_contact@naver.com");	//메일
        javaMailSender.setPassword("sky36631!!");		//패스워드

        javaMailSender.setPort(465);	//ssl의 경우 465? 네이버의 경우 465?(*확실하지는 않아 추후 다루고 수정하겠습니다)

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.starttls.trust","stmp.naver.com");	//네이버의 경우 stmp.naver.com 변경
        properties.setProperty("mail.smtp.starttls.enable","true");	//starttls <-> ssl로 변경도 확인(*starttls 와 ssl은 추후 다루도록 하겠습니다.)
        return properties;
    }
}
