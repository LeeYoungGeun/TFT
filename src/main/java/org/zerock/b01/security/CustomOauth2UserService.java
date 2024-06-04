package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRespository;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    // 소셜에 등록된 이메일을 통해서 사용자 구분을 하나, 없는 경우... 멤버 추가 설정을 위해서 DI
    private final MemberRespository memberRespository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("user Req.....................");
        log.info(userRequest.getClientRegistration());

        log.info("oauth2 user...............................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME : " + clientName); // 어떤 소셜을 사용했니?

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        String nickName = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                nickName = getKakaoNickName(paramMap);
                break;
        }

        log.info("====================================");
        log.info(email);
        log.info(nickName);
        log.info("====================================");

        log.info("oAuth2User :" + oAuth2User);

        /*return oAuth2User;*/
        return generateDTO(email, nickName, paramMap);
    }

    private MemberSecurityDTO generateDTO(String email, String nickName,Map<String, Object> params){
        Optional<Member> result = memberRespository.findByMid(email);
        Boolean existNick =  memberRespository.existsByMnick(nickName);
        String nickName_exist ;
        //닉네임이 존재하면
        if(existNick){
            nickName_exist = email;
        }else {
        //닉네임이 존재 하지 않으면
            nickName_exist = nickName;
        }

        //데이터베이스에 해당 이메일 사용자가 없는 경우...
        if(result.isEmpty()){
            //회원추가
            Member member = Member.builder()
                        .mid(email)
                        .mname(nickName)
                        .mnick(nickName_exist)
                        .mpw(passwordEncoder.encode("1111"))
                        /*.memail(email)*/
                        .mpno("01000000000")
                        .del(false)
                        .social(true)
                        .build();
            member.addRole(MemberRole.USER);
            memberRespository.save(member);
            // MemberSecurityDTO로 반환....
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email
                    ,"1111"
                    ,nickName
                    ,nickName_exist
                    /*,email*/
                    ,"01012341234"
                    ,false
                    ,true
                    , Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);
            return memberSecurityDTO;
        //데이터베이스에 해당 이메일 사용자가 있는경우
        }else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getMname(),
                    member.getMnick(),
                    member.getMpno(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream()
                            .map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                            .collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }

    }


    private String getKakaoEmail(Map<String, Object> paramMap) {

        log.info("KAKAO Email..............................");
        Object value = paramMap.get("kakao_account");
        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("email...." +  email);
        return email;
    }

    private String getKakaoNickName(Map<String, Object> paramMap) {
        log.info("KAKAO NickName..............................");
        Object value = paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;
        LinkedHashMap profileMap = (LinkedHashMap) accountMap.get("profile");

        String nickName = (String) profileMap.get("nickname");

        log.info("nickName...." +  nickName);
        return nickName;
    }



}
