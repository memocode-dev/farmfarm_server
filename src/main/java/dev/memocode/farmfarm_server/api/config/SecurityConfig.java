package dev.memocode.farmfarm_server.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(a -> a
                        // swagger
                        .requestMatchers(GET, "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
