package com.feri.sipv.sipvserver.controllers;


import com.feri.sipv.sipvserver.exceptions.ResourceNotFoundException;
import com.feri.sipv.sipvserver.models.Session;
import com.feri.sipv.sipvserver.models.User;
import com.feri.sipv.sipvserver.repositories.SessionRepository;
import com.feri.sipv.sipvserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }

        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        return ResponseEntity.ok(user);
    }

}
