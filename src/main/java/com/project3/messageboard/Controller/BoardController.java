package com.project3.messageboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러의 역할을 수행하는 클래스
public class BoardController {

    //@GetMapping: "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
    @GetMapping("/main")
    public String mainboard(){
        return "/mainboard"; //mainboard.html 띄우기
    }
}
