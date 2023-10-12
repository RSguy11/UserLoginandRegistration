package com.DisplayMakers.version02.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.DisplayMakers.version02.security.jwt.AuthEntryPoint;
import com.DisplayMakers.version02.security.jwt.AuthTokenFilter;
import com.DisplayMakers.version02.security.services.UserDetailsServiceImpl;
//config delcares class as source of bean method definitions
@Configuration
@EnableMethodSecurity(
	//allows for pre and postauthorize annotations
		prePostEnabled = true)
public class WebSecurityConfig { 

  //resposinble for retirieving user details
	@Autowired
	UserDetailsServiceImpl userDetailsService;

  //will be responsible for handiling unauthed requests
	@Autowired
	private AuthEntryPoint unauthorizedHandler;

  
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

    //sets user details and encodes password from the userdetails
	@Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }


	//used for processing auth requests
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  //encodes passwords
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

  // configuring filter chaing to enable cors(allowing cross origin reuests) and disables csrf protection (removing protection against ataccks tricks where victims perform unintended actions)
  //disabling csrf not reccomended unless using jwt tolkens because theyre constantly validated anyways
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
      //dealing with exception handling for auth failures  
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        //authentication  does not need states for tolken based
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    //    //commenting out so it starts on home page?
    //     //setting rules for different types of http request
    //     .authorizeHttpRequests(auth -> 
    //     //allowing unrestricted acess to any request with below matching patters, rquireing auth for rest
    //       auth.requestMatchers("/api/auth/**").permitAll()
    //           .requestMatchers("/api/test/**").permitAll()
    //           .anyRequest().authenticated()
    //     );
    
    // // http.authorizeHttpRequests().anyRequest().permitAll();




    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    //returns http security object representing configured filters
    return    http.build();

              
  }

}


