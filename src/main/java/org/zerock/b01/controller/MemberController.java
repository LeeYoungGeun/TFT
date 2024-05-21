package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberController {

    @GetMapping("/login")
    public void login(Model model) {

        log.info("login ..........................");

        model.addAttribute("msg", "login");
    }

    @GetMapping("/mypage")
    public void mypage(Model model) {

        log.info("mypage ..........................");

        model.addAttribute("msg", "mypage");
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
