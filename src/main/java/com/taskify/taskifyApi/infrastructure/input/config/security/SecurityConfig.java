package com.taskify.taskifyApi.infrastructure.input.config.security;

import com.taskify.taskifyApi.application.utils.JwtUtils;
import com.taskify.taskifyApi.infrastructure.config.AppProperties;
import com.taskify.taskifyApi.infrastructure.config.WebProperties;
import com.taskify.taskifyApi.infrastructure.input.config.security.filter.CustomAuthenticationEntryPoint;
import com.taskify.taskifyApi.infrastructure.input.config.security.filter.JwtTokenValidator;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@DependsOn("webProperties")
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final WebProperties webProperties;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                ).exceptionHandling(
                        ex -> ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .authorizeHttpRequests(
                        authorizeRequests -> {

                            authorizeRequests.requestMatchers(
                                    "/api/auth/**",
                                    "/api/ws/**",
                                    "/login/**",
                                    "/oauth2/**"
                            ).permitAll();

                            /*
                            Notification -> Security
                             */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/notification/v1/**")
                                            .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/notification/v1/**")
                                            .hasAnyRole("ADMIN", "USER");

                            /*
                            Images -> Security
                             */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/images/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/images/v1")
                                    .hasRole("ADMIN");
                            authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/images/v1/**")
                                    .hasRole("ADMIN");

                            /*
                            Users -> Security
                             */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/users/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.PUT, "/api/users/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/users/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/users/v1")
                                    .hasRole("ADMIN");

                            /*
                            Projects -> Security
                             */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/projects/v1")
                                    .hasRole("ADMIN");
                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/projects/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/projects/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/projects/v1")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.PUT, "/api/projects/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/projects/v1/**")
                                    .hasAnyRole("ADMIN", "USER");


                            /*
                            Task -> Security
                             */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/tasks/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/tasks/v1")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST, "/api/tasks/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.PUT, "/api/tasks/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/tasks/v1/**")
                                    .hasAnyRole("ADMIN", "USER");

                            /*
                            Files -> Security
                            */

                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/files/v1")
                                    .hasAnyRole("ADMIN");
                            authorizeRequests.requestMatchers(HttpMethod.GET, "/api/files/v1/**")
                                    .hasAnyRole("ADMIN", "USER");
                            authorizeRequests.requestMatchers(HttpMethod.POST
                                            , "/api/files/v1/**")
                                    .hasRole("ADMIN");
                            authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/files/v1/**")
                                    .hasAnyRole("ADMIN", "USER");

                            authorizeRequests.anyRequest().denyAll();
                        }
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                )
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(webProperties.getUrl());
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Location");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {

            String token = jwtUtils.createToken(authentication);

            Cookie authCookie = new Cookie("jwt", token);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(appProperties.getCookie().isSecure());
            authCookie.setPath("/");
            if (token != null) {
                long durationSeconds = appProperties.getSecurity().getJwtExpirationMs() / 1000;
                authCookie.setMaxAge((int) durationSeconds);
            } else {
                authCookie.setMaxAge(0);
            }
            authCookie.setAttribute("SameSite", "Strict");


            response.addCookie(authCookie);

            String targetUrl = webProperties.getUrl() + "/auth/callback";
            response.setHeader("Location", targetUrl);
            response.sendRedirect(targetUrl);

        };
    }
}
