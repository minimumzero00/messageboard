package com.project3.messageboard.service;

import com.project3.messageboard.entity.Messageboard;
import com.project3.messageboard.repository.MessageBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class MessageBoardService {

    @Autowired
    private MessageBoardRepository messageBoardRepository;

    //글 작성 처리
    public void write(Messageboard messageboard, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID(); //랜덤으로 이름을 만들어줌

        String fileName = uuid + "_" + file.getOriginalFilename(); //랜덤이름_파일이름
        File saveFile = new File(projectPath, fileName); // 파일생성(경로, 이름)
        file.transferTo(saveFile);

        messageboard.setFilename(fileName); //파일이름
        messageboard.setFilepath("/files/" + fileName); //저장된 파일 경로와 아름

        messageBoardRepository.save(messageboard);
    }

    // 게시글 리스트 처리
    public List<Messageboard> messageboardList() {
        return  messageBoardRepository.findAll(); //List<Messageboard> 반환
    }

    // 특정 게시글 불러오기
    public Messageboard boardView(Integer id){
        return messageBoardRepository.findById(id).get();
    }

//    public Page<Board> bordSearchList(String searchKeyword, Pageable pageable){
//
//        return boardRepository.findByTitleContaining(searchKeyword, pageable);
//    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        messageBoardRepository.deleteById(id);
    }
}

