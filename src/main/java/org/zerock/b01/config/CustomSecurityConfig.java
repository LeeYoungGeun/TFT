package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-----------------security configure--------------------");
        //로그인페이지 기본페이지에서 변경
        http.formLogin(form -> {
            form.loginPage("/member/login");
        });

        //크롬 개발자도구 네트워크 >> 엘리먼트선택 >> 페이지로드 부분에서 확인가능한 csrf(사용자인증값) 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        http.rememberMe(httpSecurityRememberMeConfigurer -> {
           httpSecurityRememberMeConfigurer.key("12345678")
                   .tokenRepository(persistentTokenRepository())
                   .userDetailsService(userDetailsService)
                   .tokenValiditySeconds(60 * 60 * 24 * 30);
        });

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("---------------web configure-------------------");
        // 정적 리소스 필터링 제외 (static 경로아래 있는 녀석들 실행 시 로그인 인증에서 제외됨)
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

}
