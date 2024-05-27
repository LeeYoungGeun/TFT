package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mypage")
    public void mypage(Model model) {

        log.info("mypage ..........................");

        model.addAttribute("msg", "mypage");
    }
//    익명인 경우에만 접근 가능하게 설정 ( 403 포비든 에러페이지가 뜨기때문에 핸들링 해줘야함)
//    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttributes) {
        log.info("login ..........................");

        //로그인한 사용자가 로그인페이지로 접근 시 처리 (컨트롤러에서 처리하는방법)
        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info(principal);
        log.info(authentication.getName());
        log.info(authentication.getAuthorities());

        if (!principal.equals("anonymousUser")) {
            //board/list 화면에 값 뿌려주어 화면에서 alert처리
            redirectAttributes.addFlashAttribute("allready", "already logged in");
            return "redirect:/board/list";
        }else {
            return "/member/login";
        }
    }


    @GetMapping("/join")
    public void join(Model model) {
        log.info("join ..........................");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {
        log.info("join POST..........................");
        log.info("memberJoinDTO : " + memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addAttribute("join", "join_success");

        return "redirect:/member/login";
    }


    @PreAuthorize("principal.username==#memberJoinDTO.mid")
    @GetMapping("/modify")
    public void modify(Model model, MemberJoinDTO memberJoinDTO) {
        log.info("modify..........................");
        log.info("memberJoinDTO.........................." + memberJoinDTO);

        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info(principal);
        log.info(authentication.getName());
        log.info(authentication.getAuthorities());

        Member detail =  memberService.getDetail(authentication.getName());
        log.info(detail);

        if (!principal.equals("anonymousUser")) {
            model.addAttribute("mid", authentication.getName());
        }
        model.addAttribute("mInfo", detail);

    }


//    @PreAuthorize("principal.username==#memberJoinDTO.mid")
    @PostMapping("/modify")
    public String modifyPost(Model model, MemberJoinDTO memberJoinDTO) {
        log.info("modifyPost..........................");
        log.info("memberJoinDTO.........................." + memberJoinDTO);

        memberService.modify(memberJoinDTO);

        return "redirect:/logout";
    }

    @PostMapping("/remove")
    public String remove(Model model ,MemberJoinDTO memberJoinDTO) {
        log.info("remove..........................");
        log.info(memberJoinDTO);

        String mid =  memberJoinDTO.getMid();
        memberService.remove(mid);

        return "redirect:/logout";
    }

}
