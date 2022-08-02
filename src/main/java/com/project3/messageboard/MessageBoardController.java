package com.project3.messageboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러의 역할을 수행하는 클래스
public class MessageBoardController {

    //메인화면
    //@GetMapping: "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
    @GetMapping("/main")
    public String main(){

        return "/main"; //main.html 띄우기
    }


    @GetMapping("/")
    public String list() {
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post() {
        return "board/post.html";
    }
}
