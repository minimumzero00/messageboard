//파일 데이터

package com.project3.messageboard.study.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter //모든 필드에 getter를 자동생성해주는 어노테이션
@Entity //객체를 테이블과 매핑할 엔티티라고 JPA에게 알려주는 역할을 하는 어노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED) //파라미터가 없는 기본 생성자를 추가하는 어노테이션(JPA 사용을 위해 기본 생성자 생성은 필수)
public class StudyFile {

    @Id //primary key
    @GeneratedValue(strategy= GenerationType.IDENTITY) //기본키로 대체키를 사용할 때, 기본키 값 생성 전략을 명시
    private Long id;

    //@Column: 컬럼을 매핑하는 어노테이션
    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @Builder //빌더패턴 클래스를 생성해주는 어노테이션
    public StudyFile(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}