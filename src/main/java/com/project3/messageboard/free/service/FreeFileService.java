package com.project3.messageboard.free.service;

import com.project3.messageboard.free.domain.entity.FreeFile;
import com.project3.messageboard.free.domain.repository.FreeFileRepository;
import com.project3.messageboard.free.dto.FreeFileDto;
import org.springframework.stereotype.Service;

//Transactional: 선언적 트랜잭션이라 부르며, 트랜잭션을 적용하는 어노테이션
import javax.transaction.Transactional;

@Service //서비스 계층임을 명시해주는 어노테이션
public class FreeFileService {
    private FreeFileRepository freeFileRepository;

    public FreeFileService(FreeFileRepository freeFileRepository) {
        this.freeFileRepository = freeFileRepository;
    }

    @Transactional
    public Long saveFile(FreeFileDto fileDto) { //saveFile(): 업로드한 파일에 대한 정보를 기록
        //save(): JpaRepository에 정의된 메서드로, DB에 INSERT, UPDATE를 담당
        return freeFileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FreeFileDto getFile(Long id) { //getFile(): id 값을 사용하여 파일에 대한 정보 가져옴
        FreeFile file = freeFileRepository.findById(id).get();

        FreeFileDto fileDto = FreeFileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}