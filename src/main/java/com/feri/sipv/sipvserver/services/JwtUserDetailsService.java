package com.feri.sipv.sipvserver.services;


import com.feri.sipv.sipvserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.feri.sipv.sipvserver.utils.JsonUtil.convertUserPermissionsJsonString;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        List<com.feri.sipv.sipvserver.models.User> users = userRepository.findAll();
        for (com.feri.sipv.sipvserver.models.User user : users) {
            if(user.getUsername() != null && user.getUsername().equals(username)){
                return new User(user.getUsername(), user.getPasswordHash(), convertUserPermissionsJsonString(user.getPermissions()));
            }
        }
        throw new UsernameNotFoundException("User with username: " + username + " does not exist.");
    }
}
