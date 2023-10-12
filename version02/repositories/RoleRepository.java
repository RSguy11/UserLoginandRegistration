package com.DisplayMakers.version02.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DisplayMakers.version02.Models.ERole;
import com.DisplayMakers.version02.Models.Role;

@Repository
//allows us to easily create, edit or deelete roles using jparepository
public interface RoleRepository extends JpaRepository<Role, Long> {
//allows us to pull up a user with a specific role
  Optional<Role> findByName(ERole name);
}