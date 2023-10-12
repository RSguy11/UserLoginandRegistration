package com.DisplayMakers.version02.Models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;




@Entity

//add, modify, or remove roles without directly affecting the user data or the structure of the "users" table
@Table(	name = "users", 
		//ensures the collumns with the following variable names need to have unique contents
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	//indicates as primary key of entiry, give collumn name id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    
	//creating columns for each variable, specifying they must contain values and cannot exceed 20 chars
	// @NotBlank
	// @Size(max = 20)
	private String username;

	// @NotBlank
	// @Size(max = 50)
	// @Email
	private String email;

	// @NotBlank
	// @Size(max = 120)
	private String password;


	//creating a relationship between the role and user tables
	//lazy determines how contents of roles db is loaded
	//(they won't be immediantly loaded when roles is retrievd, )
	@ManyToMany(fetch = FetchType.LAZY)
	//combining the two tables together
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	//constructors, getters, setters
	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
