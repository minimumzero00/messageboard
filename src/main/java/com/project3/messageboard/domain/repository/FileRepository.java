package com.project3.messageboard.domain.repository;

import com.project3.messageboard.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface FileRepository extends JpaRepository<File, Long> {
}