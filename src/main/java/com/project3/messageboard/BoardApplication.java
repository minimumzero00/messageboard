package com.project3.messageboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Bean: Spring IoC Container가 관리하는 자바 객체, Spring Bean Container에 존재하는 객체
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing //JPA Auditing 활성화를 위한 어노테이션 
@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	@Bean
	//HiddenHttpMethodFilter를 Bean으로 등록하여, @PutMapping과 @DeleteMapping이작동할 수 있도록 해줌
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

}
