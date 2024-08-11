package org.infinity.sixtalebackend.domain.member.config;

import lombok.AllArgsConstructor;
import org.infinity.sixtalebackend.domain.member.filter.JWTTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/members/auth/login").permitAll()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JWTTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/members/auth/login").permitAll()
                                .defaultSuccessUrl("/").permitAll()
                                .failureUrl("/").permitAll()
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
