package hbsh2330.security.controller;

import hbsh2330.security.config.auth.PrincipalDetails;
import hbsh2330.security.entity.User;
import hbsh2330.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

//    @GetMapping("/test/login")
//    @ResponseBody
//    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetail){
//        System.out.println("/test/login : ====================");
//        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
//        System.out.println("authentication" + principalDetails.getUser());
//        System.out.println("userDetails : " + principalDetail.getUser());
//        return "세션 정보 확인하기";
//    }
//
//    @GetMapping("/test/oauth/login")
//    @ResponseBody
//    public String testLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){
//        System.out.println("/test/login : ====================");
//        OAuth2User oAuth2 = (OAuth2User)authentication.getPrincipal();
//        System.out.println("authentication" + oAuth2.getAttributes());
//        System.out.println(oAuth2User.getAttributes());
//        return "OAuth 세션 정보 확인하기";
//    }

    @GetMapping({"", "/"})
    public String index(){
        return "index";
    }

    //O
    @GetMapping("/user") // user 로그인 한 회원만 접근 가능하게 할거임
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails" + principalDetails.getUser());
        return "user";
    }


    @GetMapping("/admin") // admin권한이 있는 사람만 가능
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager") //로그인 한사람중에 manager 권한이 있는 사람만 가능
    @ResponseBody
    public String manager(){
        return "manager";
    }


    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @GetMapping("/joinForm") // 회원가입 페이지 이동
    public String getJoinForm(){
        return "joinForm";
    }

    @PostMapping("/joinForm") // 회원가입
    @ResponseBody
    public String postJoinForm(User user){
        return userService.join(user);
    }

    @GetMapping("confirmEmail") // 이메일 확인
    @ResponseBody
    public String getConfirmEmail(@RequestParam String email){
        return userService.confirmEmail(email);
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인정보";
    }
}
