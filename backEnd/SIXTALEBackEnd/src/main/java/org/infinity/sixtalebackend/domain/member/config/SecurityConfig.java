package org.infinity.sixtalebackend.domain.member.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/members/auth/login", "/members/success", "/members/fail").permitAll()
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/members/auth/login").permitAll()
                                .defaultSuccessUrl("/members/success").permitAll()
                                .failureUrl("/members/fail").permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/members/logout")
                                .logoutSuccessUrl("/")
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
