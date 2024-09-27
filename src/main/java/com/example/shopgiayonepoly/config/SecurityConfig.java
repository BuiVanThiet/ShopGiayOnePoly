//package com.example.shopgiayonepoly.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
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
//                        .requestMatchers("/bill/**").hasAnyRole("Quản trị viên","Nhân viên bán hàng")
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/login-api/**").permitAll()
//                        .requestMatchers("/ajax/**", "/css/**", "/img/**", "/js/**", "/loading/**", "/toast/**").permitAll()
//                        .requestMatchers("/bill-api/**").permitAll()
//                        .requestMatchers("/register-api/**").permitAll()
//                        .requestMatchers("/register").permitAll()
//                        .requestMatchers("/login").permitAll()
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
//                        .defaultSuccessUrl("/home_manage", true)
//                )
//
//                .logout(config -> config
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout"))
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(accessDeniedHandler()) // Xử lý quyền truy cập bị từ chối
//                )
//                .build();
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
