package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private OAuthAuthenticationSuccessHandler handler;
	
	@Autowired
	private AuthFailureHandler authFailureHandler;

	@Bean
	UserDetailsService UserDetailServices() {
		return new CustomUserDetailServices();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(UserDetailServices());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").authenticated().anyRequest().permitAll())
				.formLogin(formlogin -> formlogin.loginPage("/signin").loginProcessingUrl("/dologin")
						.defaultSuccessUrl("/user/dashboard"))
				.logout(logout -> logout.permitAll());

		http.formLogin(auth->auth.failureHandler(authFailureHandler));
//		o-auth configuration

		http.oauth2Login(oauth -> oauth.loginPage("/signin")
				.successHandler(handler));

		return http.build();
		
		
		
	}

}
