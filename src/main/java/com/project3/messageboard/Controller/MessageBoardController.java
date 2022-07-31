package com.project3.messageboard.Controller;

import com.project3.messageboard.entity.Messageboard;
import com.project3.messageboard.service.MessageBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.multipart.MultipartFile;

//@: 어노테이션/ import 구문 상단에 자동 생성
@Controller //컨트롤러의 역할을 수행하는 클래스
public class MessageBoardController {

    //메인화면
    //@GetMapping: "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌
    @GetMapping("/main")
    public String main(){

        return "/main"; //main.html 띄우기
    }

    //게시글 작성 폼
    @Autowired
    private MessageBoardService messageBoardService;

    @GetMapping("/board/write")  //localhost:8080/board/write
    public String boardwriteForm() {
        return "boardwrite"; //boardwrite.html 뛰우기
    }

    @PostMapping("/board/writepro") //boardwite.html에 form태크 action="/board/writepro"와 일치
    public String messageboardwritePro(Messageboard messageboard, Model model, MultipartFile file) throws Exception {

        messageBoardService.write(messageboard, file);
        
        //메시지 띄우기
        model.addAttribute("message", "글 작성이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
        
    }

    //게시판 리스트
    @GetMapping("/board/list")
    public String messageboardlist(Model model,
                                   @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                   String searchKeyword) {

        Page<Messageboard> list = null;

        if(searchKeyword == null) {
            list = messageBoardService.messageboardList(pageable);
        }else{
            list = messageBoardService.bordSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1; //0에서 시작하기 때문에 1더함
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage +5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";

    }

    //게시글 상세 페이지
    @GetMapping("/board/view") //localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id){

        model.addAttribute("board",messageBoardService.boardView(id));
        return  "boardview";
    }

    //게시글 삭제
    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        messageBoardService.boardDelete(id);
        return "redirect:/board/list"; //게시글 삭제후 list로 가기
    }

    //게시글 수정
    @GetMapping("/board/modify/{id}")
    //PathVariable: {id}부분이 인식이 되서 Integer 형식의 id로 들어옴
    public String boardModify(@PathVariable("id") Integer id,  Model model) {

        model.addAttribute("board", messageBoardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Messageboard messageboard, MultipartFile file) throws Exception{

        //기존 글 가져옴
        Messageboard boardTemp = messageBoardService.boardView(id);
        //새로 입력한 내용을 기존 내용에 덮어씌움
        boardTemp.setTitle(messageboard.getTitle());
        boardTemp.setContent(messageboard.getContent());

        messageBoardService.write(boardTemp, file);

        return "redirect:/board/list";
    }

}
