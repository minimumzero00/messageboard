package com.project3.messageboard.domain.repository;

import com.project3.messageboard.domain.entity.Board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
