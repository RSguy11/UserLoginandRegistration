package com.DisplayMakers.SpringAPI.Service;
import org.springframework.stereotype.Service;

import com.DisplayMakers.SpringAPI.Models.Users;
import com.DisplayMakers.SpringAPI.Repository.UserRepository;


@Service
public class UserService {
    

    
    private final UserRepository usersRepository;


    public UserService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public Users registerUser(String login, String password, String email){
        if(login == null || password == null) {
            return null;
        }else {
            Users users = new Users();
            users.setLogin(login);
            users.setEmail(email);
            users.setPassword(password);
            return usersRepository.save(users);
            
        }
    }

    public Users authenticate(String login, String password) {
        return usersRepository.findByLoginAndPassword(login, password).orElse(null);
    }

}
