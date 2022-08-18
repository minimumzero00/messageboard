//Controller: 사용자의 HTTP 요청이 진입하는 지점, 사용자에게 서버에서 처리된 데이터를 View와 함께 응답하게 해줌

//PostMapping: URL을 매핑해주는 어노테이션 (post 방식)
//GetMapping: URL을 매핑해주는 어노테이션 (get 방식)

package com.project3.messageboard;

import com.project3.messageboard.free.dto.FreeBoardDto;
import com.project3.messageboard.free.dto.FreeFileDto;
import com.project3.messageboard.free.service.FreeBoardService;
import com.project3.messageboard.free.service.FreeFileService;
import com.project3.messageboard.free.util.FreeMD5Generator;

import com.project3.messageboard.job.dto.JobBoardDto;
import com.project3.messageboard.job.dto.JobFileDto;
import com.project3.messageboard.job.service.JobBoardService;
import com.project3.messageboard.job.service.JobFileService;
import com.project3.messageboard.job.util.JobMD5Generator;

import com.project3.messageboard.news.dto.NewsBoardDto;
import com.project3.messageboard.news.dto.NewsFileDto;
import com.project3.messageboard.news.service.NewsBoardService;
import com.project3.messageboard.news.service.NewsFileService;
import com.project3.messageboard.news.util.NewsMD5Generator;

import com.project3.messageboard.study.dto.StudyBoardDto;
import com.project3.messageboard.study.dto.StudyFileDto;
import com.project3.messageboard.study.service.StudyBoardService;
import com.project3.messageboard.study.service.StudyFileService;
import com.project3.messageboard.study.util.StudyMD5Generator;

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

    //@@@@@@@@@@@@@@@@@@@@ 메인화면 @@@@@@@@@@@@@@@@@@@@@
    // "/Mainboard"로 접근하는 url 처리를 클래스 BoardController 에서 맡는다고 알려줌

    @GetMapping("/main")
    public String main(){
        return "common/main.html"; //main.html 띄우기
    }
    
    //@@@@@@@@@@@@@@@@@@@@ 자유게시판 @@@@@@@@@@@@@@@@@@@@@
    private FreeBoardService freeBoardService;
    private FreeFileService freeFileService;

    public MessageBoardController
            (FreeBoardService freeBoardService, FreeFileService freeFileService,
             StudyBoardService studyBoardService, StudyFileService studyFileService,
             JobBoardService jobBoardService, JobFileService jobFileService,
             NewsBoardService newsBoardService, NewsFileService newsFileService) {

        this.freeBoardService = freeBoardService;
        this.freeFileService = freeFileService;

        this.studyBoardService = studyBoardService;
        this.studyFileService = studyFileService;

        this.jobBoardService = jobBoardService;
        this.jobFileService = jobFileService;

        this.newsBoardService = newsBoardService;
        this.newsFileService = newsFileService;
    }

    @GetMapping("/free/list")
    public String freelist(Model model) { //Model 객체를 통해 View에 데이터를 전달
        List<FreeBoardDto> boardDtoList = freeBoardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/free/list.html";
    }

    @GetMapping("/free/post")
    public String freepost() {
        return "board/free/post.html";
    }

    @PostMapping("/free/post")
    //boardDto: post.html에서 작성된 값 담김
    public String freewrite(@RequestParam("file") MultipartFile files, FreeBoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new FreeMD5Generator(origFilename).toString();
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

            FreeFileDto fileDto = new FreeFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = freeFileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            //boardService.savePost(boardDto)로 보냄 = DB 저장
            freeBoardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/free/list";
    }

    //게시물 클릭 시
    @GetMapping("/free/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    //PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
    public String freedetail(@PathVariable("id") Long id, Model model) {
        FreeBoardDto boardDto = freeBoardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달

        FreeFileDto fileDto = freeFileService.getFile(id);
        model.addAttribute("post2",fileDto);
        return "board/free/detail.html";
    }

    //게시글 수정
    @GetMapping("/free/post/edit/{id}") //ex) 1번 글 수정을 클릭하면 /post/edit/1로 접속
    public String freeedit(@PathVariable("id") Long id, Model model) {
        FreeBoardDto boardDto = freeBoardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/free/edit.html";
    }

    //게시글 수정 후 수정 버튼 누름
    @PutMapping("/free/post/edit/{id}")
    public String freeupdate(FreeBoardDto boardDto) { //데이터 베이스에 변경된 데이터 저장
        freeBoardService.savePost(boardDto);
        return "redirect:/free/list";
    }

    //게시글 삭제
    //@DeleteMapping: DELETE 요청을 하는 API의 어노테이션, 데이터를 삭제할 때 사용
    @DeleteMapping("/free/post/{id}") // /post/{id}에 Delete로 요청 오는 것을 처리
    public String freedelete(@PathVariable("id") Long id) {
        freeBoardService.deletePost(id);
        return "redirect:/free/list";
    }

    //파일 다운로드
    @GetMapping("/free/download/{fileId}")
    public ResponseEntity<Resource> freefileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FreeFileDto fileDto = freeFileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }

    //@@@@@@@@@@@@@@@@@@@@ 학업게시판 @@@@@@@@@@@@@@@@@@@@@
    private StudyBoardService studyBoardService;
    private StudyFileService studyFileService;

    @GetMapping("/study/list")
    public String studylist(Model model) { //Model 객체를 통해 View에 데이터를 전달
        List<StudyBoardDto> boardDtoList = studyBoardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/study/list.html";
    }

    @GetMapping("/study/post")
    public String studypost() {
        return "board/study/post.html";
    }

    @PostMapping("/study/post")
    //boardDto: post.html에서 작성된 값 담김
    public String studywrite(@RequestParam("file") MultipartFile files, StudyBoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new StudyMD5Generator(origFilename).toString();
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

            StudyFileDto fileDto = new StudyFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = studyFileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            //boardService.savePost(boardDto)로 보냄 = DB 저장
            studyBoardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/study/list";
    }

    //게시물 클릭 시
    @GetMapping("/study/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    //PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
    public String studydetail(@PathVariable("id") Long id, Model model) {
        StudyBoardDto boardDto = studyBoardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달

        StudyFileDto fileDto = studyFileService.getFile(id);
        model.addAttribute("post2",fileDto);
        return "board/study/detail.html";
    }

    //게시글 수정
    @GetMapping("/study/post/edit/{id}") //ex) 1번 글 수정을 클릭하면 /post/edit/1로 접속
    public String studyedit(@PathVariable("id") Long id, Model model) {
        StudyBoardDto boardDto = studyBoardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/study/edit.html";
    }

    //게시글 수정 후 수정 버튼 누름
    @PutMapping("/study/post/edit/{id}")
    public String studyupdate(StudyBoardDto boardDto) { //데이터 베이스에 변경된 데이터 저장
        studyBoardService.savePost(boardDto);
        return "redirect:/study/list";
    }

    //게시글 삭제
    //@DeleteMapping: DELETE 요청을 하는 API의 어노테이션, 데이터를 삭제할 때 사용
    @DeleteMapping("/study/post/{id}") // /post/{id}에 Delete로 요청 오는 것을 처리
    public String studydelete(@PathVariable("id") Long id) {
        studyBoardService.deletePost(id);
        return "redirect:/study/list";
    }

    //파일 다운로드
    @GetMapping("/study/download/{fileId}")
    public ResponseEntity<Resource> studyfileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        StudyFileDto fileDto = studyFileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }

    //@@@@@@@@@@@@@@@@@@@@ 취업게시판 @@@@@@@@@@@@@@@@@@@@@
    private JobBoardService jobBoardService;
    private JobFileService jobFileService;

    @GetMapping("/job/list")
    public String joblist(Model model) { //Model 객체를 통해 View에 데이터를 전달
        List<JobBoardDto> boardDtoList = jobBoardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/job/list.html";
    }

    @GetMapping("/job/post")
    public String jobpost() {
        return "board/job/post.html";
    }

    @PostMapping("/job/post")
    //boardDto: post.html에서 작성된 값 담김
    public String jobwrite(@RequestParam("file") MultipartFile files, JobBoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new JobMD5Generator(origFilename).toString();
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

            JobFileDto fileDto = new JobFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = jobFileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            //boardService.savePost(boardDto)로 보냄 = DB 저장
            jobBoardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/job/list";
    }

    //게시물 클릭 시
    @GetMapping("/job/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    //PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
    public String jobdetail(@PathVariable("id") Long id, Model model) {
        JobBoardDto boardDto = jobBoardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달

        JobFileDto fileDto = jobFileService.getFile(id);
        model.addAttribute("post2",fileDto);
        return "board/job/detail.html";
    }

    //게시글 수정
    @GetMapping("/job/post/edit/{id}") //ex) 1번 글 수정을 클릭하면 /post/edit/1로 접속
    public String jobedit(@PathVariable("id") Long id, Model model) {
        JobBoardDto boardDto = jobBoardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/job/edit.html";
    }

    //게시글 수정 후 수정 버튼 누름
    @PutMapping("/job/post/edit/{id}")
    public String jobupdate(JobBoardDto boardDto) { //데이터 베이스에 변경된 데이터 저장
        jobBoardService.savePost(boardDto);
        return "redirect:/job/list";
    }

    //게시글 삭제
    //@DeleteMapping: DELETE 요청을 하는 API의 어노테이션, 데이터를 삭제할 때 사용
    @DeleteMapping("/job/post/{id}") // /post/{id}에 Delete로 요청 오는 것을 처리
    public String jobdelete(@PathVariable("id") Long id) {
        jobBoardService.deletePost(id);
        return "redirect:/job/list";
    }

    //파일 다운로드
    @GetMapping("/job/download/{fileId}")
    public ResponseEntity<Resource> jobfileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        JobFileDto fileDto = jobFileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }

    //@@@@@@@@@@@@@@@@@@@@ 시사,이슈게시판 @@@@@@@@@@@@@@@@@@@@@
    private NewsBoardService newsBoardService;
    private NewsFileService newsFileService;

    @GetMapping("/news/list")
    public String newslist(Model model) { //Model 객체를 통해 View에 데이터를 전달
        List<NewsBoardDto> boardDtoList = newsBoardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/news/list.html";
    }

    @GetMapping("/news/post")
    public String newspost() {
        return "board/news/post.html";
    }

    @PostMapping("/news/post")
    //boardDto: post.html에서 작성된 값 담김
    public String newswrite(@RequestParam("file") MultipartFile files, NewsBoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new NewsMD5Generator(origFilename).toString();
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

            NewsFileDto fileDto = new NewsFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = newsFileService.saveFile(fileDto);
            boardDto.setFileId(fileId);
            //boardService.savePost(boardDto)로 보냄 = DB 저장
            newsBoardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/news/list";
    }

    //게시물 클릭 시
    @GetMapping("/news/post/{id}") //ex) 1번 글을 클릭하면 /post/1로 접속
    //PathVariable: 경로의 특정 위치 값이 고정되지 않고 달라질 때 사용
    public String newsdetail(@PathVariable("id") Long id, Model model) {
        NewsBoardDto boardDto = newsBoardService.getPost(id);
        model.addAttribute("post", boardDto); //post이름으로 detail.html에게 전달

        NewsFileDto fileDto = newsFileService.getFile(id);
        model.addAttribute("post2",fileDto);
        return "board/news/detail.html";
    }

    //게시글 수정
    @GetMapping("/news/post/edit/{id}") //ex) 1번 글 수정을 클릭하면 /post/edit/1로 접속
    public String newsedit(@PathVariable("id") Long id, Model model) {
        NewsBoardDto boardDto = newsBoardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/news/edit.html";
    }

    //게시글 수정 후 수정 버튼 누름
    @PutMapping("/news/post/edit/{id}")
    public String newsupdate(NewsBoardDto boardDto) { //데이터 베이스에 변경된 데이터 저장
        newsBoardService.savePost(boardDto);
        return "redirect:/news/list";
    }

    //게시글 삭제
    //@DeleteMapping: DELETE 요청을 하는 API의 어노테이션, 데이터를 삭제할 때 사용
    @DeleteMapping("/news/post/{id}") // /post/{id}에 Delete로 요청 오는 것을 처리
    public String newsdelete(@PathVariable("id") Long id) {
        newsBoardService.deletePost(id);
        return "redirect:/news/list";
    }

    //파일 다운로드
    @GetMapping("/news/download/{fileId}")
    public ResponseEntity<Resource> newsfileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        NewsFileDto fileDto = newsFileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
}
