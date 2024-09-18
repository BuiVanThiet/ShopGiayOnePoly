//package com.example.shopgiayonepoly.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.Arrays;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .authorizeHttpRequests( auth -> auth
//                        .requestMatchers("/bill/**").hasRole("Quản trị viên")
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/login-api/**").permitAll()
//                        .requestMatchers("/ajax/**", "/css/**", "/img/**", "/js/**", "/loading/**", "/toast/**").permitAll()
//                        .requestMatchers("/bill-api/**").permitAll()
//                        .requestMatchers("/register").permitAll()
//                        .requestMatchers("/login","/home_manage").permitAll()
//                        .requestMatchers("/logout").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(basic -> basic.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/bill/home", true)
//                )
//
//                .logout(config -> config
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout"))
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(accessDeniedHandler()) // Xử lý quyền truy cập bị từ chối
//                )
//                        .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public AccessDeniedHandler accessDeniedHandler() {
//        return new CustomAccessDeniedHandler();
//    }
//}
