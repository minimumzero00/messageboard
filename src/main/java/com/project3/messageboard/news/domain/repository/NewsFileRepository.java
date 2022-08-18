package com.project3.messageboard.news.domain.repository;

import com.project3.messageboard.news.domain.entity.NewsFile;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface NewsFileRepository extends JpaRepository<NewsFile, Long> {
}