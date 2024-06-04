package org.zerock.b01.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.zerock.b01.dto.MailDTO;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "gorea_contact@naver.com";
    private String authNum;

    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
        log.info("--------------------createMessage");
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);	//보내는 대상
        message.setSubject("회원가입 이메일 인증");		//제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요</h1>";
        msg += "<br>";
        msg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "CODE : <strong>";
        msg += authNum + "</strong>";	//메일 인증번호
        msg += "</div>";
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("gorea_contact@naver.com", "duckoo"));

        //해당 메시지는 아래에 예시
        return message;
    }

    public String createCode(){
        log.info("--------------------createCode");
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i = 0; i< 8; i++){	//인증 코드 8자리
            int index = random.nextInt(3);	//0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) ((int) random.nextInt(26) + 97));
                case 1 -> key.append((char) (int) random.nextInt(26) + 65);
                case 2 -> key.append(random.nextInt(9));
            }
        }
        return authNum = key.toString();
    }

    //메일 발송
    //등록해둔 javaMail 객체를 사용해서 이메일 send
    public String sendSimpleMessage(String sendEmail) throws Exception{
        log.info("--------------------sendSimpleMessage");
        authNum = createCode();	//랜덤 인증번호 생성

        MimeMessage message = createMessage(sendEmail);	//메일 발송
        try{
            javaMailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return authNum;
    }

}
