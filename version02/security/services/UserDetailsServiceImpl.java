package com.DisplayMakers.version02.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.DisplayMakers.version02.Models.User;
import com.DisplayMakers.version02.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;
  
    @Override
    //transactional treats this method as a transaction, therefore rolling back any changes made if exception occurs
    @Transactional
    //this class provides a method for loading user specific data given a username
    //pairs with userdetailsutil
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
  
      return UserDetailsImpl.build(user);
    }
  
  }
  