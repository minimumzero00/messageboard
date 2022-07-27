package com.project3.messageboard.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Messageboard { //DB이름과 같게 만들어줌

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY: mysql, mariadb에서 사용
    private Integer id; //mysql 테이블 안에 보드 중 id

    private String title;

    private String content;
}
