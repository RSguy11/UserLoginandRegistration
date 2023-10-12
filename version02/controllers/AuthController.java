package com.DisplayMakers.version02.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DisplayMakers.version02.Models.ERole;
import com.DisplayMakers.version02.Models.Role;
import com.DisplayMakers.version02.Models.User;
import com.DisplayMakers.version02.payload.request.LoginRequest;
import com.DisplayMakers.version02.payload.request.SignupRequest;
import com.DisplayMakers.version02.payload.response.JwtResponse;
import com.DisplayMakers.version02.payload.response.MessageResponse;
import com.DisplayMakers.version02.repositories.RoleRepository;
import com.DisplayMakers.version02.repositories.UserRepository;
import com.DisplayMakers.version02.security.jwt.JwtUtils;
import com.DisplayMakers.version02.security.services.UserDetailsImpl;

import jakarta.validation.Valid;





@CrossOrigin(origins = "*", maxAge = 3600)
//marks as controller (handles http requests), and responsebody (return values converted to appropiate format)
// @RestController
// @RequestMapping("/api/auth")
@Controller

public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	//setup for authentication, retrieving user info,  retrieving role info, and generating jwt for authentification
	@Autowired
	UserRepository userRepository;

	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;


	//getmapping indicates that this function handles when http tries to GET /register by supplying the name of the html page to be viewed
    @GetMapping("/register") 
    public String getRegistrationPage(Model model) {
        //adds empty users to the model to be used
        model.addAttribute("registerRequest", new User());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model ) {
        model.addAttribute("loginRequest", new User());
        return "login_page"; 
    }



	//authenticateUser method responds to POST requests from sign ing, takes in loginrquest
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		//creating auth object through authmanager
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		//it may or may not makes the auth object able to acess the details inside, but i cant find a single example using it like this
		//retrieves current auth object associated with security context to acesss the Principal and other user details stored within the Authentication object.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//jwt token generated
		String jwt = jwtUtils.generateJwtToken(authentication);
		//principal object () cast to userdetailsImpl 
		// The princiapl is the current identity of the user, represented here by a userDetails, however, we cast it to a  userDetailsImpl
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		//roles retrieved to transport in  the jwtresponse
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		//marking as ok, sending back info to js to use for personalizing expirience maybe??
		//yeah cuz they didn't give us the id when logging in but now we have it
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}



	@PostMapping("/register")
	//return type responseentity, allows us to return objects and spring mvc will handle serialization to JSON
	//valid indicates it needs the paramater needs to be validate, requestbody  indicateds method paramater should be 
	//takes in registerRequest, so this func will activate whenever the client sends a post to/register that matches the structure signup request class
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest registerRequest) {
		//checking if name has been taken and sending badrequest(response status 400)
		//js code can deal with this response when reacting to fetch of /api/register
		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		//same for dupe email
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account from paramater data
		User user = new User(registerRequest.getUsername(), 
							 registerRequest.getEmail(),
							 encoder.encode(registerRequest.getPassword()));


		//storing the role in a String set (as multiple roles can be assigned to one user)
		Set<String> strRoles = registerRequest.getRole();
		//creating set to use for transfering data to user object
		Set<Role> roles = new HashSet<>();
		//if theres nothing set, default to user role
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
			//handlin scenario where role cannot be found in role repository, error condition indicated
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			//going through each role and adding to set
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		//role set
		user.setRoles(roles);
		//user saved in repository
		userRepository.save(user);
		//200 status given to indicate user registered sucessfully
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}



   
        

}
