package com.finance.dashboard.security;

import com.finance.dashboard.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RoleAuthFilter roleAuthFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // User management — ADMIN only
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Records — write — ADMIN only
                        .requestMatchers(HttpMethod.POST,   "/api/records").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/records/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/records/**").hasRole("ADMIN")

                        // Records — read — all roles
                        .requestMatchers(HttpMethod.GET, "/api/records/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/records")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        // Dashboard — all roles
                        // Requirement: "Viewer: Can only view dashboard data"
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, e) -> {
                            response.setStatus(403);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(new ErrorResponse(
                                            403, "Forbidden",
                                            "Role not permitted to " + request.getMethod()
                                                    + " " + request.getRequestURI())));
                        })
                        .authenticationEntryPoint((request, response, e) -> {
                            response.setStatus(403);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(new ErrorResponse(
                                            403, "Forbidden", "X-Role header is required")));
                        })
                )

                .addFilterBefore(roleAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}