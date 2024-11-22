package ca.gbc.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] noauthResourceUris={
            "/swagger-ui",
            "/swagger-ui/*",
            "v3/api-docks/**",
            "swagger-resource/**",
            "/api-docs/**",
            "/aggregate/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        log.info("Initializing Security Filter Chain");

        return httpSecurity
                .csrf(AbstractHttpConfigurer:: disable) //disable protection - don't do irl
 //               .authorizeHttpRequests(authorize -> authorize
   //                     .anyRequest().permitAll()) //all requests temporarily allowed - block when done debugging
              .authorizeHttpRequests(authorize -> authorize
                      .requestMatchers(noauthResourceUris)
                      .permitAll()
                      .anyRequest().authenticated()) //all requests require authentication
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .build();

    }
}
