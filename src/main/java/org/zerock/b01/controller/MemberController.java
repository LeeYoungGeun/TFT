package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/mypage")
    public void mypage(Model model) {

        log.info("mypage ..........................");

        model.addAttribute("msg", "mypage");
    }

    @GetMapping("/login")
    public void login(Model model) {
        log.info("login ..........................");
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

        redirectAttributes.addAttribute("result", "success");

        return "redirect:/member/login";
    }



    @GetMapping("/modify")
    public void modify(Model model) {
        log.info("modify ..........................");
        model.addAttribute("msg", "modify");
    }

    @GetMapping("/delete")
    public void delete(Model model) {
        log.info("delete ..........................");
        model.addAttribute("msg", "delete");
    }

}
