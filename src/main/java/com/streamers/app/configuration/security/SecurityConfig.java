package com.streamers.app.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
@Order(1)
public class SecurityConfig
{

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //             .authorizeHttpRequests((authz) -> authz
    //                     .anyRequest().permitAll()
    //             )
    //             .httpBasic(withDefaults());
    //     return http.build();
    // }
}
