package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.BoardDTO;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

        @Autowired
    private BoardService boardService;

        @Test
    public void testRegister(){
            log.info(boardService.getClass().getName());

            BoardDTO boardDTO = BoardDTO.builder()
                    .title("test test test")
                    .content("test content test content")
                    .writer("test test writer")
                    .build();

            Long bno = boardService.register(boardDTO);
            log.info("bno : "+ bno);
        }


        @Test
    public void testModify(){

            BoardDTO boardDTO = BoardDTO.builder()
                    .bno(402L)
                    .title("바꾼다 test")
                    .content("바꾼다 writer")
                    .build();

            boardService.modify(boardDTO);
        }

        @Test
    public void testRemove(){
            boardService.remove(402L);
        }


}
