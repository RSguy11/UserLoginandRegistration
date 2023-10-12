package com.DisplayMakers.version02.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DisplayMakers.version02.Models.User;

//indicates following is used for data acess, non-functional
@Repository
//jpaRepository provids CRUD (create, read update delete) options for user, long specified as its the variable type of the primary key

public interface UserRepository extends JpaRepository<User, Long> {
    //optional thefore potentionally null return type, if found User with matching username returned
    Optional<User> findByUsername(String username);


    //true returned if username exists in db
    Boolean existsByUsername(String username);
    
    //true if email exists in db
    Boolean existsByEmail(String email);
}