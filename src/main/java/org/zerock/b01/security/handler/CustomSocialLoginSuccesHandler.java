package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b01.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccesHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("-------------------------------------------------");
        log.info("CustomLoginSuccesHandler OnAuthenticationSuccess");
        log.info("-------------------------------------------------");

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        log.info("memberSecurityDTO : " + memberSecurityDTO);

        //소셜 로그인이고 회원 패스워드 1111
        if(memberSecurityDTO.isSocial()&& !memberSecurityDTO.isDel() &&
                passwordEncoder.matches("1111",memberSecurityDTO.getMpw() ) ){
            log.info("Should Change Password");
            log.info("Redirect to Mmeber Modify");
            response.sendRedirect("/member/modify");
        }else {
            response.sendRedirect("/board/list");
        }

    }
}
