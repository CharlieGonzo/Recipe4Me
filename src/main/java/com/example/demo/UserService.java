package com.example.demo;


import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class UserService {



    private UserRepository rep;

    public UserService(UserRepository rep){

        this.rep = rep;
    }

    public User saveUser(User user){
       return rep.save(user);
    }

    public Optional<User> getUser(Long id){
        return rep.findById(id);
    }

    public void deleteUser(Long id){
        rep.deleteById(id);
    }

    public User createUser(String username,String password){
        return rep.save(new User(username,password));
    }

    public User findUserByUsername(String name){
        return rep.findUserByUsername(name);
    }


}
