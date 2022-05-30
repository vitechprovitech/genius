package com.faguy.genius.securite;



import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

	@Autowired private DataSource datasource;
	
	  @Override protected void configure(AuthenticationManagerBuilder auth) throws
	  Exception { // TODO Auto-generated method stub
		  	auth.jdbcAuthentication().dataSource(datasource)
		  	.usersByUsernameQuery("select email as principal, password as credentials, name from user where email=?")
		  	.authoritiesByUsernameQuery("select login as principal, role as role where login=? ")
		  	.passwordEncoder(new  MessageDigestPasswordEncoder("MD5"))
		  	.rolePrefix("ROLE_");
		  	 System.err.println("bonjour ------> ");
	  }
	  
	  @Override protected void configure(HttpSecurity http) throws Exception { 
		  http.authorizeRequests()
		
          .antMatchers("/android/*").permitAll();
		  http.formLogin().loginPage("/login");
		  http.csrf().disable();
		 

	  }

	 

}
