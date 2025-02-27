package com.example.top.pages.config;

import com.example.top.pages.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/api/v1/rate").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/items").authenticated()
                        .requestMatchers("api/v1/user/info").authenticated()
                        .requestMatchers("api/items/users_rated_items").authenticated()
                        .requestMatchers("/api/v1/rate/approve").hasRole("ADMIN")
                        .requestMatchers("/api/v1/rate/action").authenticated()
                        .requestMatchers("api/v1/history").authenticated()
                        .requestMatchers("/api/v1/rate/disprove").hasRole("ADMIN")
                        .requestMatchers("/api/items/approve").hasRole("ADMIN")
                        .requestMatchers("/api/items/disprove").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/items").authenticated()
                        .requestMatchers(HttpMethod.GET, "api/v1/requests").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/pages/join_item").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/pages").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "api/v1/user").authenticated()
                        .requestMatchers(HttpMethod.PUT, "api/v1/user/change_password").authenticated()
                        .requestMatchers("/api/v1/pages/approve").hasRole("ADMIN")
                        .requestMatchers("/api/v1/pages/disprove").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pages").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/category").hasRole("ADMIN")
                        .requestMatchers("api/v1/pages/create").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
 }
