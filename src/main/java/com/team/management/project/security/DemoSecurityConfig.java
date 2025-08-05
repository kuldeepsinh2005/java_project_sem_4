package com.team.management.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.team.management.project.service.PlayerServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import com.team.management.project.security.CustomSuccessHandler;

import java.util.List;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig {

	@Autowired
	private PlayerServiceImpl playerServiceImpl;
	
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) {

	    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

	    // Use your custom player table
	    jdbcUserDetailsManager.setUsersByUsernameQuery(
	        "SELECT username, password, true FROM player WHERE username = ?"
	    );
	    //true because all player that are in table are always active. 
	    jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
	        "SELECT username, role FROM player WHERE username = ?"
	    );

	    return jdbcUserDetailsManager;
	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http
        .authorizeHttpRequests(configurer -> 
            configurer
            .requestMatchers("/").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/players/register-solo","/players/create-team").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/logout").permitAll()
            .requestMatchers("/teams/update","/teams/delete","/api/auth/leader-page","/api/requests/accept","/api/requests/reject").hasRole("LEADER")
            .requestMatchers("/api/auth/solo-page","/api/auth/member-page","/api/requests/page","/api/requests/player/addrequest","/players/delete").hasAnyRole("SNIPER","HEALER","RUSHER")
            .requestMatchers("/players/update","/teams/team-all","/players/all-list","/teams/remove-player").hasAnyRole("LEADER","SNIPER","HEALER","RUSHER")
            .requestMatchers("/players/dashboard").hasAnyRole("ADMIN","LEADER","SNIPER","HEALER","RUSHER")
           ).formLogin(form -> form
        	  .defaultSuccessUrl("/players/dashboard", true)
              .permitAll()
           );

        return http.build();
    }
	@Bean
	public UserDetailsService userDetailsService() {
		return playerServiceImpl;
	}
	@Bean
	 public AuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		 provider.setUserDetailsService(playerServiceImpl);
		 provider.setPasswordEncoder(passwordEncoder());
		 return provider;
	 }
	 
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}













