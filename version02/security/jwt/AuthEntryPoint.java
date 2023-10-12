package com.DisplayMakers.version02.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//allows for custom bean detection and dependancy injection
@Component
//this code will handle unauthorized attempts and login errors
//sends back json with error info to client
public class AuthEntryPoint implements AuthenticationEntryPoint {
    //logs error messages
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);
    //commence method handles auth failures, necessary with implementation
  public void commence(HttpServletRequest request, HttpServletResponse response,
  org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
    //message logged 
    logger.error("Unauthorized error: {}", authException.getMessage());
    //json to be sent back
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    //specifiying auth error
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //creating hashmap for body of error, filling with info
    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());
    //creating the place to serialize the body into a json
    final ObjectMapper mapper = new ObjectMapper();
    //json error response sent out
    mapper.writeValue(response.getOutputStream(), body);

  }
}
