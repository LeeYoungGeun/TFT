package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO); // 작성

    ReplyDTO read(Long rno);    // 읽기

    void modify(ReplyDTO replyDTO); // 수정

    void remove(Long rno);  // 삭제

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno,  // 댓글목록페이징
                                             PageRequestDTO pageRequestDTO);

}
