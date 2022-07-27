package com.project3.messageboard.Controller;

import com.project3.messageboard.entity.Messageboard;
import com.project3.messageboard.service.MessageBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러의 역할을 수행하는 클래스
public class MessageBoardController {

    //메인화면
    //@GetMapping: "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
    @GetMapping("/main")
    public String mainboard(){
        return "/mainboard"; //mainboard.html 띄우기
    }

    //게시글 작성 폼
    @Autowired
    private MessageBoardService messageBoardService;

    @GetMapping("/board/write")  //localhost:8080/board/write
    public String boardwriteForm() {
        return "boardwrite"; //boardwrite.html 뛰우기
    }

    @PostMapping("/board/writepro") //boardwite.html에 form태크 action="/board/writepro"와 일치
    public String messageboardwritePro(Messageboard messageboard) {

        messageBoardService.write(messageboard);

        return "";
    }

}
