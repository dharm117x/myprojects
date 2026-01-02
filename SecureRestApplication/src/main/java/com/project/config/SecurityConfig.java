package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
        .csrf(csrf -> csrf.disable())
        
        // 👇 FIX FOR X-FRAME-OPTIONS
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
        )

        .authorizeHttpRequests(auth -> auth
            // Swagger
            .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/index.html"
            ).permitAll()

            // H2 Console
            .requestMatchers("/h2-console/**").permitAll()
            
            // APIs
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/**").hasAnyRole("USER","ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {

		UserDetails adminUser = User.builder()
	            .username("admin")
	            .password(passwordEncoder().encode("admin123"))
	            .roles("ADMIN", "USER") // 👈 MULTIPLE ROLES
	            .build();

	    UserDetails normalUser = User.builder()
	            .username("user")
	            .password(passwordEncoder().encode("user123"))
	            .roles("USER")
	            .build();

	    return new InMemoryUserDetailsManager(adminUser, normalUser);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		
}
