package com.mustache.bbs1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()// 기본값 UI -> 비활성화
                .csrf().disable()// CSRF 보안 필요 시 -> 비활성화
                .cors().and()// cross 사이트에서 도메인이 다를 때 허용해주는 것
                .authorizeRequests()
                .antMatchers("**articles**", "**hospital**").permitAll() // join, login은 언제나 가능
                .antMatchers(HttpMethod.POST, "/api/v1/visits").authenticated() // 접근 요청 막기, permitAll 다음에 입력해줘야함, 순서대로 진행되기 때문
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                // UsernamePasswordAuthenticationFilter 앞에다가 토큰 필터를 넣는것
                .addFilterBefore(new JwtTokenFilter(secretKey), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .build();
    }
}
