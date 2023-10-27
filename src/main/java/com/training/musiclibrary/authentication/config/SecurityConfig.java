package com.training.musiclibrary.authentication.config;

import com.training.musiclibrary.authentication.models.enums.Role;
import com.training.musiclibrary.utils.constants.Endpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity // Tells Spring that this is a Security config file
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // To fix : https://github.com/jzheaux/cve-2023-34035-mitigations/tree/main
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

        // -- TODO, to ERASE, Used for h2 console -- start
        http.authorizeHttpRequests().requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll();
        http.headers().frameOptions().disable();
        http.csrf(csrf->csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
        // -- TODO, to ERASE, Used for h2 console -- end

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(mvc.pattern(Endpoints.AUTH_PREFIX+Endpoints.SIGNUP)).permitAll()
                        .requestMatchers(mvc.pattern(Endpoints.AUTH_PREFIX+Endpoints.LOGIN)).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, Endpoints.SONGS+"/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, Endpoints.SONGS)).hasRole(Role.ARTIST.name())
                        .requestMatchers(mvc.pattern(HttpMethod.GET, Endpoints.USERS+"/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, Endpoints.USERS+"**")).hasRole(Role.ADMIN.name())
                        .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()
                        .requestMatchers(mvc.pattern("/api-docs/**")).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .logout(logout -> logout
                .logoutSuccessUrl(Endpoints.AUTH_PREFIX + Endpoints.LOGIN)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        http.cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:3000/login");
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
