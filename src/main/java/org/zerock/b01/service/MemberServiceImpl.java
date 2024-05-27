package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.repository.MemberRespository;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRespository memberRespository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        log.info("Member Service Imp join================================");

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRespository.existsById(mid);

        if (exist){
            log.info("--------------------중복");
            throw  new MidExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info(member);
        log.info(member.getRoleSet());

        memberRespository.save(member);
    }

    @Override
    public void modify(MemberJoinDTO memberJoinDTO) {
        log.info("Member service impl modify --------------------------------------");

        // modelMapper로 memberJoinDTO에 있는 값을 Member 클래스로 변환.... 반환.
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));  // 패스워드 변경.. 인코딩 처리...

        log.info(member);

        memberRespository.save(member);
    }


    @Override
    public Member getDetail(String mid) {
        Optional<Member> result =  memberRespository.findById(mid);

        Member member = result.orElseThrow();

        log.info("Member service impl getDetail --------------------------------------");
        log.info(member);
        return member;
    }

    @Override
    public void remove(String mid) {
        log.info("Member service impl remove --------------------------------------");
        log.info(mid);

        Member member = getDetail(mid);
        member.changeDel(true);

        log.info(member);

        memberRespository.save(member);
    }
}
