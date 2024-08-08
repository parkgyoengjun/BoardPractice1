package com.myproject.boardpractice1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing // 엔티티 객체가 생성 되거나 변경 되었을때 @EnableJpaAuditing 을 활용하여 자동으로 값을 등록
@Configuration // 설정 클래스이므로 빈에 등록한다.
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() { // 현재 인증기능이 따로 없어서 생성자, 수정자에 uno 라는 이름을 넣어서 보내주는것
        return () -> Optional.of("uno"); // TODO : 스프링 시큐리티로 인증 기능을 붙이게 될때 수정
    }
    // 생성자 수정자에 현 상태로는 값이 없어 임의로 uno 를 넣어줌
    // AuditorAware :

//    @Bean
//    public AuditorAware<String> auditorAware() {
//        return new AuditorAware<String>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
//                return Optional.of("uno");
//            }
//        };

    }
