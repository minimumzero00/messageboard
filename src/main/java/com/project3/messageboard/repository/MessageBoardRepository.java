package com.project3.messageboard.repository;

import com.project3.messageboard.entity.Messageboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageBoardRepository extends JpaRepository <Messageboard, Integer> {

    Page<Messageboard> findByTitleContaining(String searchKeyword, Pageable pageable);
}
