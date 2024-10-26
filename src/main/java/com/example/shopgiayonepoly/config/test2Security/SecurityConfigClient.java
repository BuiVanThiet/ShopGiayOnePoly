package com.example.shopgiayonepoly.config.test2Security;

import com.example.shopgiayonepoly.config.CustomAccessDeniedHandler;
import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.implement.ClientSecuritiImplement;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfigClient {
    @Autowired
    ClientSecurityResponsetory clientSecurityResponsetory;
    @Autowired
    ClientSecuritiImplement clientSecuritiImplement;
    @Bean
    public SecurityFilterChain customerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/onepoly/products").hasAnyRole("Quản trị viên", "Nhân viên bán hàng")
                        .requestMatchers("/onepoly/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .userDetailsService(clientSecuritiImplement)
                .formLogin(form -> form
                        .loginPage("/onepoly/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/onepoly/home", true)
                        .failureHandler(authenticationFailureHandlerClient())  // Xử lý khi đăng nhập thất bại
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/onepoly/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandlerClient())  // Xử lý khi quyền bị từ chối
                );


        return http.build();
    }

    // Xử lý khi đăng nhập thất bại
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandlerClient() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException exception) throws IOException, ServletException {
                String username = request.getParameter("username");
                request.getSession().setAttribute("usernameFalse", username);

                // Kiểm tra tài khoản có tồn tại không
                ClientLoginResponse staff = clientSecurityResponsetory.getCustomerByEmailAndAcount(username,username);
                if (staff != null) {
                    // Tài khoản tồn tại nhưng mật khẩu không đúng
                    request.getSession().setAttribute("usernameError", null);
                    request.getSession().setAttribute("passwordError", "Mật khẩu không chính xác");
                } else {
                    // Tài khoản không tồn tại
                    request.getSession().setAttribute("usernameError", "Tài khoản không tồn tại");
                    request.getSession().setAttribute("passwordError", "Mật khẩu không chính xác");
                }

                // Chuyển hướng lại trang login với thông báo lỗi
                super.setDefaultFailureUrl("/login?error=true");
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }

    // Xử lý khi quyền bị từ chối
    @Bean
    public AccessDeniedHandler accessDeniedHandlerClient() {
        return new CustomAccessDeniedHandler();
    }
}
