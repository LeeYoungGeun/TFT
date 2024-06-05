package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccesHandler;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // HTTP 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-----------------security configure--------------------");

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            // 접근 거부 핸들러 등록
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
        });

        // 로그인 페이지 및 로그인 성공 후 페이지 설정
        http.formLogin(form -> { // 폼 기반 로그인 활성화
            form.loginPage("/member/login")
                    // 로그인 페이지 및 로그인 성공 후 페이지
                    .defaultSuccessUrl("/board/list");
        });

        //크롬 개발자도구 네트워크 >> 엘리먼트선택 >> 페이지로드 부분에서 확인가능한 csrf(사용자인증값) 비활성화
        // CSRF 보안 기능 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // Remember-Me 기능 설정
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
           httpSecurityRememberMeConfigurer.key("123456789") // 키 설정
                   .tokenRepository(persistentTokenRepository()) // 토큰 저장소 설정
                   .userDetailsService(userDetailsService) // 사용자 디테일 서비스 설정
                   .tokenValiditySeconds(60 * 60 * 24 * 30); // 토큰 유효 시간 설정 ( 30일 )
        });

        // OAuth2 로그인 설정
        http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/member/login"); // 로그인 페이지 설정
            httpSecurityOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler()); // 로그인 성공 핸들러 설정
        });

        return http.build();
    }

    // 소셜 로그인 성공 핸들러 빈 등록
   @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccesHandler(passwordEncoder);
    }

    // 접근 거부 핸들러 빈 등록
    //AccessDeniedHandler 빈등록
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    // 정적 리소스 필터링을 위한 웹 보안 커스터마이저 빈 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("---------------web configure-------------------");
        // 정적 리소스 필터링 제외 (static 경로아래 있는 녀석들 실행 시 로그인 인증에서 제외됨)
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    // PersistentTokenRepository 빈 등록
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
