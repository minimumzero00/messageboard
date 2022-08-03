package com.project3.messageboard;

import com.project3.messageboard.dto.BoardDto;
import com.project3.messageboard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러의 역할을 수행하는 클래스
public class MessageBoardController {

    //메인화면
    //@GetMapping: "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
    @GetMapping("/main")
    public String main(){

        return "/main"; //main.html 띄우기
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private BoardService boardService;

    public MessageBoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post() {
        return "board/post.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

}
