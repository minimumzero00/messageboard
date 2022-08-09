//Controller: 사용자의 HTTP 요청이 진입하는 지점, 사용자에게 서버에서 처리된 데이터를 View와 함께 응답하게 해줌

package com.project3.messageboard;

import com.project3.messageboard.dto.BoardDto;
import com.project3.messageboard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//GetMapping: URL을 매핑해주는 어노테이션 (get 방식)
import org.springframework.web.bind.annotation.GetMapping;
//PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
import org.springframework.web.bind.annotation.PathVariable;
//PostMapping: URL을 매핑해주는 어노테이션 (post 방식)
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러임을 명시하는 어노테이션
public class MessageBoardController {

    //메인화면
    // "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
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
    public String list(Model model) { //Model 객체를 통해 View에 데이터를 전달
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post() {
        return "board/post.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) { //boardDto: post.html에서 작성된 값 담김
        boardService.savePost(boardDto); //boardService.savePost(boardDto)로 보냄 = DB 저장
        return "redirect:/";
    }

    //게시물 클릭 시
    @GetMapping("/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달
        return "board/detail.html";
    }

    //게시글 수정
    @GetMapping("/post/edit/{id}") //ex) 1번 글 수정을 클릭하면 /post/edit/1로 접속
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit.html"; 
    }

    //게시글 수정 후 수정 버튼 누름
    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) { //데이터 베이스에 변경된 데이터 저장
        boardService.savePost(boardDto);
        return "redirect:/";
    }
}
