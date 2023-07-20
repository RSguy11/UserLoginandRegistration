package com.DisplayMakers.SpringAPI.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DisplayMakers.SpringAPI.Models.Users;


public interface UserRepository extends JpaRepository<Users ,Integer> {
    
    Optional<Users> findByLoginAndPassword(String login, String password);
}
