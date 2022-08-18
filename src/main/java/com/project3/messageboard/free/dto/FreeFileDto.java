package com.project3.messageboard.free.dto;


import com.project3.messageboard.free.domain.entity.FreeFile;
import lombok.*;

@Getter //모든 필드에 getter를 자동생성해주는 어노테이션
@Setter
@ToString
@NoArgsConstructor //파라미터가 없는 기본 생성자를 추가하는 어노테이션
public class FreeFileDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    //toEntity(): dto에서 필요한 부분을 빌더 패턴을 통해 entity로 만듦
    public FreeFile toEntity() {
        FreeFile build = FreeFile.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder //빌더패턴 클래스를 생성해주는 어노테이션
    public FreeFileDto(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
