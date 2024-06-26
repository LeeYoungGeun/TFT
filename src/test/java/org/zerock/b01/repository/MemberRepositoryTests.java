package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRespository memberRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMemberTest() {
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .mid("user" + i + "@naver.com")
                    .mpw(passwordEncoder.encode("1111"))
                    .mname("김이박" + i)
                    .mnick("닉네임" + i)
                    .mpno("0101234000"+i)
                    .build();
            member.addRole(MemberRole.USER);  //권한 설정

            if(i >= 90) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRespository.save(member);
        });
    }

}
