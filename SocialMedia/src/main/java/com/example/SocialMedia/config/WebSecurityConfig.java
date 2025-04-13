package com.example.SocialMedia.config;

import com.example.SocialMedia.dto.response.ResponseObject;
import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.utils.JwtTokenFilter;
import com.example.SocialMedia.utils.MapHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Autowired
    UserRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        //Find user by username or phone or email
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> userOp = userRepository.findUserByUsername(username);
                if(userOp.isPresent()){
                    return userOp.get();
                }
                if (username.contains("@"))
                    return userRepository.findUserByEmail(username)
                            .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
                else
                    return userRepository.findUserByPhone(username)
                            .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

            }
        };
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Disable CSRF và session
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Authorize requests
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/login/**",
                                "/api/v1/forgot-password",
                                "/api/v1/user/create",
                                "/api/v1/user/verify",
                                "/api/v1/user/send-verify").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/**").hasRole("CUSTOMER")
                        .requestMatchers("/**").permitAll()
                );

        // Exception handling - access denied
        http.exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, accessDeniedException.getMessage());
                    Map<String, Object> map = MapHelper.convertObject(responseObject);
                    response.getWriter().write(new JSONObject(map).toString());
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    ResponseObject responseObject = new ResponseObject(HttpStatus.UNAUTHORIZED, authException.getMessage());
                    Map<String, Object> map = MapHelper.convertObject(responseObject);
                    response.getWriter().write(new JSONObject(map).toString());
                })
        );

        // Add JWT filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    protected CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
