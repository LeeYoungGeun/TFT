package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.b01.config.PasswordEncoderConfig;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoderConfig passwordEncoderConfig;
    //private PasswordEncoder passwordEncoder;  // 주입하면 순환구조 발생 final 제거

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername  : " + username);

        //userDeatils 객체로 반환하는 userDetails를 생성
        UserDetails userDetails = User.builder().username("user1")
                .password(passwordEncoderConfig.passwordEncoder().encode("1111")).authorities("ROLE_USER").build();

        return userDetails;
    }
}