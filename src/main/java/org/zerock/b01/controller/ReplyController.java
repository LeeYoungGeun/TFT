package org.zerock.b01.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;  //의존성 주입

    @Operation(summary = "Repies POST - POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)

    public Map<String,Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult) throws BindException {

        log.info(replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }


    @Operation(summary = "Replies of Board로 GET방식으로 특정 게시물의 댓글 목록 처리하기")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(

            @PathVariable("bno") Long bno,
            PageRequestDTO pageRequestDTO) {

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno,pageRequestDTO);

        return responseDTO;

    }


    @Operation(summary = "Read Reply - GET 방식으로 특정 댓글 조회하기")
    @GetMapping("/{rno}")
    public ReplyDTO getREplyDTO( @PathVariable("rno") Long rno ){

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/remove/{rno}")
    public void remove(@PathVariable("rno") Long rno ) {
        log.info("remove--------------------------------------");
        replyService.remove(rno);
    }


    @Operation(summary = "Modify Reply - PUT 방식으로 특정 댓글 수정하기")
    @PutMapping("/{rno}")
    public Map<String,Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {

        log.info("modify--------------------------------------");
        log.info(rno);

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String,Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;

    }
}