package com.project3.messageboard.free.domain.repository;

import com.project3.messageboard.free.domain.entity.FreeFile;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface FreeFileRepository extends JpaRepository<FreeFile, Long> {
}