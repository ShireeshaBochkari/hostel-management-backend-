package com.hostel.hostel_management.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return email -> {
            com.hostel.hostel_management.entity.User user =
                    userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new UsernameNotFoundException(
                                            "User not found: " + email));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


@Bean
public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        CorsConfigurationSource corsConfigurationSource,
        JwtAuthenticationFilter jwtAuthenticationFilter)
        throws Exception {

    http
        .cors(cors ->
            cors.configurationSource(corsConfigurationSource))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
                            
                    .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(
                            jakarta.servlet.DispatcherType.ERROR
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()


            // Rooms - available and student's room
            .requestMatchers(HttpMethod.GET, "/api/rooms/available").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/rooms/my-room").hasRole("HOSTELER")
            .requestMatchers("/api/rooms/**").hasAnyRole("WARDEN", "ADMIN")

            // Hostellers
            .requestMatchers(HttpMethod.GET, "/api/hostellers/me", "/api/hostellers/my-room").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/hostellers/me").authenticated()
            .requestMatchers("/api/hostellers/**").hasAnyRole("WARDEN", "ADMIN")

            // Notices
            .requestMatchers(HttpMethod.GET, "/api/notices").authenticated()
            .requestMatchers("/api/notices/**").hasAnyRole("WARDEN", "ADMIN")

            // Other controllers have @PreAuthorize checks
            .requestMatchers("/api/complaints/**").authenticated()
            .requestMatchers("/api/visitors/**").authenticated()
            .requestMatchers("/api/payments/**").authenticated()
            .requestMatchers("/api/messages/**").authenticated()
            .requestMatchers("/api/dashboard/**").authenticated()

            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
}


@Bean
public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
        JwtAuthenticationFilter filter) {

    FilterRegistrationBean<JwtAuthenticationFilter> registration =
            new FilterRegistrationBean<>();

    registration.setFilter(filter);
    registration.setEnabled(false);

    return registration;
}
    
}
