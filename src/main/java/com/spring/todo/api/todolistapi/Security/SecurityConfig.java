package com.spring.todo.api.todolistapi.Security;

import com.spring.todo.api.todolistapi.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// This class is used to configure the security of the application
@Configuration
public class SecurityConfig {

    // It defines which endpoints are public and which are private
    // It also defines which roles can access which endpoints
    @Bean
    SecurityFilterChain springWebFilterChain(HttpSecurity http,
            JwtTokenProvider tokenProvider) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/items/signin").permitAll()
                        .requestMatchers("/swagger-ui-custom.html").permitAll()
                        .requestMatchers("/api-docs").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/items/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/items/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/items/**").hasRole("USER")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    // This bean is used to configure the user details service
    // Besides this bean is used to enable the spring security to use the custom
    // user details service

    @Bean
    UserDetailsService customUserDetailsService(UserRepository users) {
        return (username) -> users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }

    // This bean is used to configure the authentication manager
    // It returns an UsernamePasswordAuthenticationToken object

    @Bean
    AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService) {
        return authentication -> {
            String username = authentication.getPrincipal() + "";

            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (!user.isEnabled()) {
                throw new DisabledException("User account is not active");
            }

            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        };
    }
}
