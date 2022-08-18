//DTO(Data Access Object): DTO를 통하여 Controller와 Service 사이의 데이터를 주고받음

package com.project3.messageboard.free.dto;

import com.project3.messageboard.free.domain.entity.FreeBoard;
import lombok.*;

import java.time.LocalDateTime;

@Getter //모든 필드에 getter를 자동생성해주는 어노테이션
@Setter
@ToString
@NoArgsConstructor //파라미터가 없는 기본 생성자를 추가하는 어노테이션
public class FreeBoardDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private Long fileId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //toEntity(): dto에서 필요한 부분을 빌더 패턴을 통해 entity로 만듦
    public FreeBoard toEntity() {
        FreeBoard build = FreeBoard.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .fileId(fileId)
                .build();
        return build;
    }

    @Builder //빌더패턴 클래스를 생성해주는 어노테이션
    public FreeBoardDto(Long id, String author, String title, String content, Long fileId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}

