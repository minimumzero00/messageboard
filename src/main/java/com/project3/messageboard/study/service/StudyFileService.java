package com.project3.messageboard.study.service;

import com.project3.messageboard.study.domain.entity.StudyFile;
import com.project3.messageboard.study.domain.repository.StudyFileRepository;
import com.project3.messageboard.study.dto.StudyFileDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service //서비스 계층임을 명시해주는 어노테이션
public class StudyFileService {
    private StudyFileRepository freeFileRepository;

    public StudyFileService(StudyFileRepository freeFileRepository) {
        this.freeFileRepository = freeFileRepository;
    }

    @Transactional
    public Long saveFile(StudyFileDto fileDto) { //saveFile(): 업로드한 파일에 대한 정보를 기록
        //save(): JpaRepository에 정의된 메서드로, DB에 INSERT, UPDATE를 담당
        return freeFileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public StudyFileDto getFile(Long id) { //getFile(): id 값을 사용하여 파일에 대한 정보 가져옴
        StudyFile file = freeFileRepository.findById(id).get();

        StudyFileDto fileDto = StudyFileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}