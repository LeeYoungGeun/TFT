package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.config.PasswordEncoderConfig;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MailDTO;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MailService;
import org.zerock.b01.service.MemberService;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public void mypage(Model model) {

        log.info("mypage ..........................");

        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Member detail =  memberService.getDetail(authentication.getName());
        log.info(detail);
        model.addAttribute("mNicName", detail.getMnick());

    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String login(Model model, RedirectAttributes redirectAttributes, String error) {
        log.info("login ..........................");
        log.info("error : "  + error);

        if (error != null && error.equals("ACCESS_DENIED")){
            log.info("ACCESS_DENIED ..........................");
            return "/error/403_hdr";
        } else  if (error != null && error.equals("UN_KNOWN")){
            log.info("UN_KNOWN................................");
            return "/error/unknown_hdr";
        }

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
        } catch (MemberService.MnickExistException e) {
            redirectAttributes.addFlashAttribute("error", "mnick");
            return "redirect:/member/join";
        }

        redirectAttributes.addAttribute("join", "join_success");

        return "redirect:/logout";
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("principal.username==#memberJoinDTO.mid")
    @PostMapping("/modify")
    public String modifyPost(Model model, MemberJoinDTO memberJoinDTO) {
        log.info("modifyPost..........................");
        log.info("memberJoinDTO.........................." + memberJoinDTO);

        memberService.modify(memberJoinDTO);

        return "redirect:/logout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkPw")
    public void checkPw(Model model , boolean isRemoveReq) {
        log.info("checkPw..........................");
        log.info("isRemoveReq : " + isRemoveReq);

        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        Member detail =  memberService.getDetail(authentication.getName());
        log.info(detail);
        model.addAttribute("mNicName", detail.getMnick());

        model.addAttribute("isRemoveReq", isRemoveReq);
    }

    @PostMapping("/checkPw")
    public String checkPwPost(Model model, MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes, boolean isRemoveReq) {
        log.info("checkPwPost..........................");
        log.info(memberJoinDTO);
        log.info("isRemoveReq : " + isRemoveReq );

        Authentication authentication   = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());

        String mpw_login =  memberService.getDetail(authentication.getName()).getMpw();
        String paramMpw = memberJoinDTO.getMpw();

        //삭제가 아닌경우 (회원 정보 수정인 경우)
        if (!isRemoveReq){
            if(passwordEncoderConfig.passwordEncoder().matches(paramMpw,mpw_login)){
                log.info("checkPwPost pass..........................");
                return "redirect:/member/modify";

            }else {
                log.info("checkPwPost not pass..........................");
                redirectAttributes.addFlashAttribute("checkPw", "pwError");
                return "redirect:/member/checkPw";
            }
        //삭제인 경우 (회원 삭제인 경우)
        }else {
            if(passwordEncoderConfig.passwordEncoder().matches(paramMpw,mpw_login)){
                log.info("checkPwPost pass..........................");
                redirectAttributes.addAttribute("mid", memberJoinDTO.getMid());
                return "redirect:/member/remove";
            }else {
                log.info("checkPwPost not pass..........................");
                redirectAttributes.addAttribute("isRemoveReq", isRemoveReq);
                redirectAttributes.addAttribute("mid", memberJoinDTO.getMid());
                redirectAttributes.addFlashAttribute("checkPw", "pwError");
                return "redirect:/member/checkPw";
            }
        }
    }

    @PreAuthorize("principal.username==#mid")
    @GetMapping("/remove")
    public String remove(Model model ,String mid) {
        log.info("remove..........................");
        log.info(mid);
        memberService.remove(mid);

        return "redirect:/logout";
    }
}
