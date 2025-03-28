package com.example.bt4.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfiguration(
			JwtAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationProvider authenticationProvider
			) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(List.of("http://localhost:8080"));
		configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/login/**").permitAll()
						.requestMatchers("/user/**").permitAll()
						.requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
						.requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
						.anyRequest()
						.authenticated()
					)
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						)
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
				
	}
}
