package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.Member;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRespository memberRespository;

    @Test
    public void insertMemberTest() {
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .mid("member" + i )
                    .mpw("1111")
                    .mname("김이박" + i)
                    .mnick("닉네임" + i)
                    .memail("email"+i+"@naver.com")
                    .mpno("0101234000"+i)
                    .build();
            memberRespository.save(member);
        });
    }
}
