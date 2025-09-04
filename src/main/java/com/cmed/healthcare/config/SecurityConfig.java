package com.cmed.healthcare.config;

import com.cmed.healthcare.model.User;
import com.cmed.healthcare.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepo;

    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Only enabled users can login
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
                .filter(User::isEnabled) // must be true in DB
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getUsername())
                        .password(u.getPassword()) // must be BCrypt encoded
                        .roles(u.getRole())        // must match DB role exactly
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found or not approved"));
    }

    //  @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }


    @Bean
public PasswordEncoder passwordEncoder() {
    // Raw password use korar jonno
    return new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString(); // Encode na kore raw return
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword); // Raw comparison
        }
    };
}


    // Redirect users based on role after login
    @Bean
    public AuthenticationSuccessHandler mySuccessHandler() {
        return (request, response, authentication) -> {
            User currentUser = userRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found after login"));

            String role = currentUser.getRole();
            if ("DOCTOR".equals(role)) {
                response.sendRedirect("/index.html");
            } else if ("PHARMACIST".equals(role) || "MEDICAL_STAFF".equals(role)) {
                response.sendRedirect("/staff.html");
            } else {
                response.sendRedirect("/index.html");
            }
        };
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/login.html",
                        "/signup.html",
                        "/css/**",
                        "/js/**",
                        "/h2-console/**",
                        "/api/v1/auth/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(mySuccessHandler()) // redirect based on role
                .failureUrl("/login.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .permitAll()
            );

        return http.build();
    }
} 