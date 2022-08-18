package com.project3.messageboard.job.service;

import com.project3.messageboard.job.domain.entity.JobFile;
import com.project3.messageboard.job.domain.repository.JobFileRepository;
import com.project3.messageboard.job.dto.JobFileDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service //서비스 계층임을 명시해주는 어노테이션
public class JobFileService {
    private JobFileRepository freeFileRepository;

    public JobFileService(JobFileRepository freeFileRepository) {
        this.freeFileRepository = freeFileRepository;
    }

    @Transactional
    public Long saveFile(JobFileDto fileDto) { //saveFile(): 업로드한 파일에 대한 정보를 기록
        //save(): JpaRepository에 정의된 메서드로, DB에 INSERT, UPDATE를 담당
        return freeFileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public JobFileDto getFile(Long id) { //getFile(): id 값을 사용하여 파일에 대한 정보 가져옴
        JobFile file = freeFileRepository.findById(id).get();

        JobFileDto fileDto = JobFileDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}