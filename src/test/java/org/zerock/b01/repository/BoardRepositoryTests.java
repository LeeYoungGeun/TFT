package org.zerock.b01.repository;


import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.service.BoardService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void insertTest(){
        Board board = Board.builder()
                .title("test12345678")
                .writer("testWriter12345678")
                .content("testContent12345678")
                .build();

        boardRepository.save(board);
    }

    @Test
    public void selectTests(){
        Long bno = 106L;

         Optional<Board> result = boardRepository.findById(bno);

         Board board = result.orElseThrow();

         log.info(board);


    }

    @Test
    public void modifyTests(){
        Long bno = 106L;

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("변경해버리기테스트","변경해버리기 테스트~!");

        boardRepository.save(board);
        log.info(board);
    }

    @Test
    public void deleteTest(){

        boardRepository.deleteById(106L);
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : " + result.getTotalElements());
        log.info("total pages : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());

        List<Board> todoResult = result.getContent();

        todoResult.forEach(board -> log.info(board));
    }

    @Test
    public void testSearchAll() {
        String[] type = {"t", "w", "c"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(type, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image test")
                .content("첨부파일 테스트11")
                .writer("tester")
                .build();
        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "야이새끼야!!" + i + ".jpg");
        }
        boardRepository.save(board);
    }


    @Test
    public void testSearchReplyCount() {

        String[] types = {"t", "w", "c"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.
                searchWithReplyCount(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        //pag size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));

    }


    @Transactional
    @Test
    public void testReadWithImage() {
        Optional<Board> result = boardRepository.findById(112L);
        Board board = result.orElseThrow();


        log.info(board);
        log.info("------------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }

    }
    @Test
    public void testListWithAll(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null){
                for (BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()) {
                    log.info(boardImageDTO);
                }
            }
            log.info("=================================git");

        });
    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardRepository.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample...Title....")
                .content("Sample Content.....")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                ));
        Long bno = boardService.register(boardDTO);

        log.info("bno : "+ bno);

    }

}
