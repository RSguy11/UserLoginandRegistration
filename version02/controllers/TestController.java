package com.DisplayMakers.version02.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//enables CORS (cross origin resource sharing) allowing web applications from other domains to request this api
//origins set to all
//max age is the secodns for preflight response that can be cached
@CrossOrigin(origins = "*", maxAge = 3600)
//marks as controller (handles http requests), converts return value to body of http response
@RestController
//this class is specifically for endpoints startingwith /api/test
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
    //handles GET requests for /api/test/all
	@GetMapping("/user")
	//only users with specific roles can acess this endpoint
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
        //places string below at this endpoint
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
