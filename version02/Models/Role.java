package com.DisplayMakers.version02.Models;


import jakarta.persistence.*;

//setting the following class as an entity, specifically a table that will automtatically be created in the db indicated in the application properties
@Entity
//roll table will simply match user ids to their role type
@Table(name = "roles")
public class Role {
	
    //random int set for ID
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    //specifieing these valeues are strings
	@Enumerated(EnumType.STRING)
    //@column does not create the column, as the creation of the instance variable does that
    //but this allows us to specfic a length limit for this collumn
	@Column(length = 20)
    //roll type can only be one of 4 from Erole Class

	private ERole name;

	public Role() {

	}

	public Role(ERole name) {
		this.name = name;
	}
    //getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
}    


