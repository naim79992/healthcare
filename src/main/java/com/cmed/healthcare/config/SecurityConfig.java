package com.cmed.healthcare.config;

import com.cmed.healthcare.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepo;

    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // password hashing
    }

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//     http
//         .csrf(csrf -> csrf.disable())
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/api/v1/auth/**", "/login.html", "/signup.html", "/css/**", "/js/**", "/h2-console/**").permitAll()
//             .anyRequest().authenticated()
//         )
//         .formLogin(form -> form
//     .loginPage("/login.html")
//     .loginProcessingUrl("/login")          // POST action URL your form uses
//     .usernameParameter("username")         // form field for username
//     .passwordParameter("password")         // form field for password
//     .defaultSuccessUrl("/index.html", true)
//     .failureUrl("/login.html?error=true")
//     .permitAll()
// )

//         .logout(logout -> logout.permitAll());

//     return http.build();
// }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // disable CSRF for simple login
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // allow H2 console
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login.html", "/signup.html", "/css/**", "/js/**", "/h2-console/**", "/api/v1/auth/**")
            .permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login.html")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/index.html", true)
            .failureUrl("/login.html?error=true")
            .permitAll()
        )
        .logout(logout -> logout.permitAll());

    return http.build();
}

}
