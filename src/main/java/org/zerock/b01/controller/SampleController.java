package org.zerock.b01.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Controller
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello ..........................");

        model.addAttribute("msg", "HELLO WORLD!!!");
    }

    @GetMapping("/index")
    public void index(Model model) {
        log.info("index ..........................");

        model.addAttribute("msg", "index!!!");
    }

    @GetMapping("/layout/basic")
    public void basic(Model model) {
        log.info("basic ..........................");

        model.addAttribute("msg", "basic!!!");
    }

    @GetMapping("/error/403_hdr")
    public void hdr_404(Model model) {
        log.info("403 ..........................");

        model.addAttribute("403", "403!!!");
    }

    @GetMapping("/error/unknown_hdr")
    public void hdr_unknown(Model model) {
        log.info("unknown ..........................");

        model.addAttribute("unknown", "unknown!!!");
    }

    //thymeleaf 연습
    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        List<String> list = Arrays.asList("AAA","BBB","CCC","DDD");

        model.addAttribute("list",list);
    }

}
