package com.example.bookmanagement.Utils;

import com.example.bookmanagement.Utils.Oauth2.CustomOauth2Service;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Data
public class SecurityConfig {
    private final CustomOauth2Service customOauth2Service;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(author -> {
                            author.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                            author.requestMatchers("/login/**", "/img/**", "/oauth2/**", "/do-login").permitAll();
                            author.anyRequest().authenticated();
                        }
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login.loginPage("/login")
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2Service))
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .formLogin(login -> login.loginPage("/login").loginProcessingUrl("/do-login").permitAll())
                .logout(logout ->
                        logout.invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")
                                .permitAll())
                .sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login")
                )
                .build();
    }
}
