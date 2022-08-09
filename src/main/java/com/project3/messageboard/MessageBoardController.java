//Controller: 사용자의 HTTP 요청이 진입하는 지점, 사용자에게 서버에서 처리된 데이터를 View와 함께 응답하게 해줌

//PostMapping: URL을 매핑해주는 어노테이션 (post 방식)
//GetMapping: URL을 매핑해주는 어노테이션 (get 방식)

package com.project3.messageboard;

import com.project3.messageboard.dto.BoardDto;
import com.project3.messageboard.dto.FileDto;
import com.project3.messageboard.service.BoardService;
import com.project3.messageboard.service.FileService;
import com.project3.messageboard.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private FileService fileService;

    public MessageBoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
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
    //boardDto: post.html에서 작성된 값 담김
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            //boardService.savePost(boardDto)로 보냄 = DB 저장
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    //게시물 클릭 시
    @GetMapping("/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    //PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달

        FileDto fileDto = fileService.getFile(id);
        model.addAttribute("post2",fileDto);
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

    //게시글 삭제
    //@DeleteMapping: DELETE 요청을 하는 API의 어노테이션, 데이터를 삭제할 때 사용
    @DeleteMapping("/post/{id}") // /post/{id}에 Delete로 요청 오는 것을 처리
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }

    //파일 다운로드
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
}
