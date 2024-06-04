package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.service.MemberService;

@Log4j2
@RequiredArgsConstructor
@Component("getPrincipalInMnick")
public class CustomPrincipalHandler {

    private final MemberService memberService;

    public Boolean getPrincipalInMnick(String mid, String writer){
        log.info("-------------------------------getPrincipalInMnick");
        log.info("mid" + mid);
        log.info("writer : " + writer);
        Member detail =  memberService.getDetail(mid);
        log.info("mNickName : " + detail.getMnick());
        log.info(detail.getMnick().equals(writer));
        return detail.getMnick().equals(writer);
    }
}
