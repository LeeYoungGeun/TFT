package org.zerock.b01.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/*
  스프링 시큐리티에서는 UserDetails 라는 인터페이스를 사용합니다.
  때문에 스프링 시큐리티에서 UserDetails의 구현체인 User를 상속받아서 구현....
 */
@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String mid;
    private String mpw;
    private String mname;
    private String mnick;
    /*private String memail;*/
    private String mpno;
    private boolean del;
    private boolean social;

    private Map<String, Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(String username, String password,
                             String mname, String mnick, /*String memail,*/ String mpno,
                             boolean del, boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
        this.mname = mname;
        this.mnick = mnick;
        /*this.memail = memail;*/
        this.mpno = mpno;
        this.del = del;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
