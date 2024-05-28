package org.zerock.b01.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardLike;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardLikeRepository;
import org.zerock.b01.repository.BoardRepository;
import org.zerock.b01.repository.MemberRespository;
import org.zerock.b01.repository.ReplyRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;
    private final MemberRespository memberRespository;
    private final BoardLikeRepository boardLikeRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO boardDTO) {

        Board board =dtoToEntity(boardDTO);

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;

    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        board.clearImages();

        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 입니다."));

        boardLikeRepository.deleteByBoard(board);

        replyRepository.deleteByBoard(board);

        boardRepository.deleteById(bno);
    }


    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream().map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getType();

        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        for (BoardListAllDTO dto : result.getContent()) {
            Long likeCount = (long) countLikes(dto.getBno());
            dto.setLikeCount(likeCount);
        }

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public void likeBoard(BoardLikeDTO boardLikeDTO) {
        Board board = boardRepository.findById(boardLikeDTO.getBoard_bno()).orElseThrow(() -> new IllegalArgumentException("잘못된 게시판 입니다."));
        Member member = memberRespository.findById(boardLikeDTO.getMember_mid()).orElseThrow(() -> new IllegalArgumentException("잘못된 ID 입니다."));

        Optional<BoardLike> existingLike = boardLikeRepository.findByBoardAndMember(board, member);
        if(existingLike.isPresent()){
            boardLikeRepository.delete(existingLike.get());
        }else{
            BoardLike boardLike = BoardLike.builder()
                    .board(board)
                    .member(member)
                    .build();
            boardLikeRepository.save(boardLike);
        }
    }

    @Override
    public int countLikes(Long board_bno) {
        Board board = boardRepository.findById(board_bno).orElseThrow(() -> new IllegalArgumentException("잘못된 게시판 입니다."));
        return boardLikeRepository.countByBoard(board);
    }


}
