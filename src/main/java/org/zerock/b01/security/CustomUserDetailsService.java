package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.b01.config.PasswordEncoderConfig;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRespository;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

//    private final PasswordEncoderConfig passwordEncoderConfig;
    //private PasswordEncoder passwordEncoder;  // 주입하면 순환구조 발생 final 제거

    private final MemberRespository memberRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername  : " + username);

        //userDeatils 객체로 반환하는 userDetails를 생성 (모든 로그인처리가 user1로 처리됨)
        /*UserDetails userDetails = User.builder().username("user1")
                .password(passwordEncoderConfig.passwordEncoder().encode("1234")).authorities("ROLE_USER").build();
        return userDetails;*/

        // DB에 등록된 사용자 정보를 불러옴....
        Optional<Member> result = memberRespository.getWithRoles(username);

        // 결과가 없는 경우에... UserDetails에 있는 예외 처리 클래스를 호출....
        if(result.isEmpty()) {
            throw new UsernameNotFoundException("username not found.....");
        }

        Member member = result.get();

        //UserDetails 객체로 반환하는 userDetails를 생성...
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getMname(),
                member.getMnick(),
                member.getMemail(),
                member.getMpno(),
                member.isDel(),
                false,       // 소셜로 로그인 처리하지 않는 상황
                member.getRoleSet().stream().map(memberRole ->
                                new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                        .collect(Collectors.toList())

        );

        log.info("memberSecurityDTO : ");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}