package com.project3.messageboard.job.domain.repository;

import com.project3.messageboard.job.domain.entity.JobFile;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface JobFileRepository extends JpaRepository<JobFile, Long> {
}