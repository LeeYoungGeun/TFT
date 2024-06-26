package org.zerock.b01.controller;



import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.BoardService;
import org.zerock.b01.service.MemberService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Value("C:\\upload")
    private String uploadPath;

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("/list")
    public void setLog(PageRequestDTO pageRequestDTO, Model model) {
//       PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public void registerGet(Model model){
        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Member detail =  memberService.getDetail(authentication.getName());
        log.info(detail);
        model.addAttribute("mNicName", detail.getMnick());
    }

    @PreAuthorize("hasAnyRole({'ROLE_USER','ROLE_ADMIN'})")
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        /*Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Member detail =  memberService.getDetail(authentication.getName());
        log.info(detail);
        boardDTO.setWriter(detail.getMnick());*/

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);
        redirectAttributes.addFlashAttribute("result", "register");

        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Member detail =  memberService.getDetail(authentication.getName());
        log.info("authentication----------------------");
        log.info(authentication.getPrincipal());
        log.info(detail);
        model.addAttribute("mNicName", detail.getMnick());

        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);

        int likes = boardService.countLikes(bno);

        model.addAttribute("dto",boardDTO);
        model.addAttribute("likes", likes);
    }

    /*@PreAuthorize("principal.username==#boardDTO.writer")*/
    @PreAuthorize("@getPrincipalInMnick.getPrincipalInMnick(principal.username,#boardDTO.writer)")
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result","modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";

    }

    /*@PreAuthorize("principal.username==#boardDTO.writer")*/
    @PreAuthorize("@getPrincipalInMnick.getPrincipalInMnick(principal.username,#boardDTO.writer)")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        log.info("REMOVE----------------------------------------------");
        log.info("bno : "+ boardDTO.getBno());


        boardService.remove(boardDTO.getBno());


        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result","removed");

        return "redirect:/board/list";
    }

    public void removeFiles(List<String> files){

        for (String fileName:files) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();


            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();


                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public String likeBoard(@RequestParam("bno")Long bno, @RequestParam("member_mid")String member_mid, RedirectAttributes redirectAttributes){
        BoardLikeDTO boardLikeDTO = new BoardLikeDTO();
        boardLikeDTO.setBoard_bno(bno);
        boardLikeDTO.setMember_mid(member_mid);

        boardService.likeBoard(boardLikeDTO);

        redirectAttributes.addAttribute("bno", bno);
        return "redirect:/board/read";
    }



}