//Repository: 데이터 조작을 담당하며, JpaRepository를 상속

package com.project3.messageboard.domain.repository;

import com.project3.messageboard.domain.entity.Board;

import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository의 값은 매핑할 Entity와 Id의 타입
public interface BoardRepository extends JpaRepository<Board, Long> {
}
