//service: 비즈니스 로직을 구현
//데이터 처리(모델)를 담당하는 repository에서 데이터를 가져와서 controller에 넘겨주거나, 비즈니스 로직을 처리

package com.project3.messageboard.job.service;

import com.project3.messageboard.job.domain.entity.JobBoard;
import com.project3.messageboard.job.domain.repository.JobBoardRepository;
import com.project3.messageboard.job.dto.JobBoardDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service //서비스 계층임을 명시해주는 어노테이션
public class JobBoardService {
    private JobBoardRepository freeBoardRepository;

    public JobBoardService(JobBoardRepository freeBoardRepository) {
        this.freeBoardRepository = freeBoardRepository;
    }

    @Transactional
    public Long savePost(JobBoardDto boardDto) {
        //save(): JpaRepository에 정의된 메서드로, DB에 INSERT, UPDATE를 담당
        return freeBoardRepository.save(boardDto.toEntity()).getId();
    }

    //게시글 목록 가져오기
    //Repository에서 모든 데이터를 조회하여, BoardDto List에 데이터를 넣어 반환
    @Transactional
    public List<JobBoardDto> getBoardList() {
        List<JobBoard> boardList = freeBoardRepository.findAll();
        List<JobBoardDto> boardDtoList = new ArrayList<>();

        for(JobBoard board : boardList) {
            JobBoardDto boardDto = JobBoardDto.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    //게시글 클릭 시 게시물 내용 화면 출력
    @Transactional
    public JobBoardDto getPost(Long id) {
        //findById(): PK 값을 where 조건으로 하여, 데이터를 가져오기 위한 메서드
        JobBoard board = freeBoardRepository.findById(id).get();

        JobBoardDto boardDto = JobBoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .fileId(board.getFileId())
                .createdDate(board.getCreatedDate())
                .build();
        return boardDto;
    }

    //게시물 삭제 버튼 누름
    //1번 글에서 '삭제' 버튼을 클릭하면 /post/1로 접속
    @Transactional
    public void deletePost(Long id) {
        //deleteById(): PK값을 where 조건으로 하여, 데이터를 삭제하기 위한 메서드이며, JpaRepository 인터페이스에서 정의
        freeBoardRepository.deleteById(id);
    }
}
