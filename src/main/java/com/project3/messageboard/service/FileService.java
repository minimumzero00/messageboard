package com.project3.messageboard.service;

import com.project3.messageboard.domain.entity.File;
import com.project3.messageboard.domain.repository.FileRepository;
import com.project3.messageboard.dto.FileDto;
import org.springframework.stereotype.Service;

//Transactional: 선언적 트랜잭션이라 부르며, 트랜잭션을 적용하는 어노테이션
import javax.transaction.Transactional;

@Service //서비스 계층임을 명시해주는 어노테이션
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto) { //saveFile(): 업로드한 파일에 대한 정보를 기록
        //save(): JpaRepository에 정의된 메서드로, DB에 INSERT, UPDATE를 담당
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id) { //getFile(): id 값을 사용하여 파일에 대한 정보 가져옴
        File file = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}