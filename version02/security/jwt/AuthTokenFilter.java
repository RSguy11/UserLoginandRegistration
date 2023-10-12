package com.DisplayMakers.version02.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.DisplayMakers.version02.security.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//class purpose to intercept hhtp requests and authenticate based on json web tokens
//once per filter request makes sure that the filter is only executed onc, maintains consitent behavior
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
  
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
  
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
  
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
      try {
        //extracts jwt token
        String jwt = parseJwt(request);
        //checks if valid
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          //retrieves user details
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          //auth token is created useing details and its role (authority)
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          
          //authentication established
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        //error message logged to debug easily int  the futre, following bit is just a placeholder
        logger.error("Cannot set user authentication: {}", e);
      }
      //filter chain(sequence of filters before http request reaches application endpoint) continues
      filterChain.doFilter(request, response);
    }




  //jwt extracted from request header
    private String parseJwt(HttpServletRequest request) {
      String headerAuth = request.getHeader("Authorization");
      //returns substring representing JWT excluding "bearer"
      if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
        return headerAuth.substring(7, headerAuth.length());
      }
  
      return null;
    }
  }
  