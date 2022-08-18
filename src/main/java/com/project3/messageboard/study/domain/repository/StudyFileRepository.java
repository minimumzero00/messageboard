package com.project3.messageboard.study.domain.repository;

import com.project3.messageboard.study.domain.entity.StudyFile;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface StudyFileRepository extends JpaRepository<StudyFile, Long> {
}