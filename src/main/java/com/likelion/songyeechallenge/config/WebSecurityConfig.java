package com.likelion.songyeechallenge.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests((authz)-> {
                        try {
                            authz
                                    .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                    .antMatchers("/home/**", "/api/v1/user/**", "/h2-console/**").permitAll()
                                    .anyRequest().authenticated()
                            .and()
                                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            .and()
                                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}