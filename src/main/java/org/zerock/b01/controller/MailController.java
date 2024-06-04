package org.zerock.b01.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.MailDTO;
import org.zerock.b01.dto.ResponseDto;
import org.zerock.b01.service.MailService;
import org.zerock.b01.service.MemberService;
import org.zerock.b01.utils.ResponseUtil;

import java.util.NoSuchElementException;

@Log4j2
/*@RestController*/
@Controller
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MemberService memberService;
    private final MailService mailService;

    @PostMapping("/email-test")
    public String mailTest(HttpSession httpSession,  String email) throws Exception{
        log.info("email-validate-----------------------------");
        log.info("email : " + email);
        String code = mailService.sendSimpleMessage("gusrhks712@gmail.com");
        System.out.println("인증코드 : " + code);
        httpSession.setAttribute("code", code);
        return "redirect:/member/join";
    }

    @PostMapping("/email-validate")
    public ResponseDto<String> mailConfirm(HttpSession httpSession,  @RequestBody String email) throws Exception{
        log.info("email-validate-----------------------------");
        log.info("email : " + email);
        String code = mailService.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);
        httpSession.setAttribute("code", code);
        return ResponseUtil.SUCCESS("이메일 인증 메일이 전송되었습니다.","200");
    }

    @PostMapping("/email-cert")
    public ResponseDto<String> mailCert(HttpSession httpSession, @PathVariable(value = "code", required = false) String code, @RequestBody MailDTO mailDTO) throws Exception{
        log.info("email-cert-----------------------------");
        log.info("code : " + code);
        log.info("mailDTO : " + mailDTO);
        if(httpSession.getAttribute("code").equals(mailDTO.getCertCode())){
            return ResponseUtil.SUCCESS("이메일 인증이 성공하였습니다.","200");
        }else{
            throw new NoSuchElementException("인증번호가 일치하지 않습니다.");
        }
    }
}
