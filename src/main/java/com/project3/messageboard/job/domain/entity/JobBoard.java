//Entity: DB 테이블과 매핑되는 객체를 정의

package com.project3.messageboard.job.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter //모든 필드에 getter를 자동생성해주는 어노테이션
@Entity //객체를 테이블과 매핑할 엔티티라고 JPA에게 알려주는 역할을 하는 어노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED) //파라미터가 없는 기본 생성자를 추가하는 어노테이션(JPA 사용을 위해 기본 생성자 생성은 필수)
@EntityListeners(AuditingEntityListener.class) //JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알림
public class JobBoard {

    @Id //primary key
    @GeneratedValue(strategy= GenerationType.IDENTITY) //기본키로 대체키를 사용할 때, 기본키 값 생성 전략을 명시
    private Long id;

    //@Column: 컬럼을 매핑하는 어노테이션
    @Column(length = 10, nullable = false)
    private String author;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private Long fileId;

    @CreatedDate //Entity가 처음 저장될 때 생성일을 주입하는 어노테이션
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate //Entity가 수정될 때 수정 일자를 주입하는 어노테이션
    private LocalDateTime modifiedDate;

    @Builder //빌더패턴 클래스를 생성해주는 어노테이션
    public JobBoard(Long id, String author, String title, String content, Long fileId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
    }
}
