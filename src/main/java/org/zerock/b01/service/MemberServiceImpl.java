package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.repository.MemberRespository;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRespository memberRespository;
    private final ModelMapper modelMapper;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRespository.existsById(mid);

        if (exist){
            log.info("중복");
            throw  new MidExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class);

        log.info(member);

        memberRespository.save(member);
    }
}
