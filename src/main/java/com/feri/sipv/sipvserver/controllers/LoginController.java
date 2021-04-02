package com.feri.sipv.sipvserver.controllers;

import com.feri.sipv.sipvserver.exceptions.RejectedException;
import com.feri.sipv.sipvserver.exceptions.ResourceNotFoundException;
import com.feri.sipv.sipvserver.models.Activity;
import com.feri.sipv.sipvserver.models.JWT.JwtRequest;
import com.feri.sipv.sipvserver.models.JWT.JwtResponse;
import com.feri.sipv.sipvserver.models.Session;
import com.feri.sipv.sipvserver.models.User;
import com.feri.sipv.sipvserver.models.helpers.RegistrationRequest;
import com.feri.sipv.sipvserver.repositories.ActivityRepository;
import com.feri.sipv.sipvserver.repositories.SessionRepository;
import com.feri.sipv.sipvserver.repositories.UserRepository;
import com.feri.sipv.sipvserver.services.JwtUserDetailsService;
import com.feri.sipv.sipvserver.utils.JwtTokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ActivityRepository activityRepository;


    @RequestMapping(value = "/api/v1/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerNewUser(@RequestBody RegistrationRequest registrationRequest) throws RejectedException {
        if(registrationRequest.firstName == null) throw new RejectedException("First name is mandatory!");
        if(registrationRequest.lastName == null) throw new RejectedException("Last name is mandatory!");
        if(registrationRequest.username == null) throw new RejectedException("Username is mandatory!");
        if(registrationRequest.password == null) throw new RejectedException("Password is mandatory!");

        List<User> users = userRepository.findAll();
        for (User user : users){
            if(user.getUsername().equals(registrationRequest.username))
                throw new RejectedException("Username is taken!");
        }

        User newUser = new User(registrationRequest.firstName, registrationRequest.lastName, registrationRequest.username, "");
        newUser.setId(UUID.randomUUID());
        String passHash = DigestUtils.sha256Hex(newUser.getId() + registrationRequest.password);
        newUser.setPasswordHash(passHash);
        newUser.setPermissions("{permissions:[PROFILE_PERMISSION, PERSONAL_FOODS_PERMISSION, INTAKE_PERMISSION]}");

        activityRepository.save(new Activity(newUser.getId(), System.currentTimeMillis() / 1000L, "New user registered: " + newUser.getFirstName() + " " + newUser.getLastName() + "; " + newUser.getId(), false));

        userRepository.save(newUser);

        return ResponseEntity.ok("Successfully registered new user");
    }


    @RequestMapping(value = "/api/v1/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String[] tokenInfo = jwtTokenUtil.generateToken(userDetails);
        List<User> allUsers = userRepository.findAll();
        JwtResponse response = null;
        for (User userFromList : allUsers) {
            if (userFromList.getUsername() != null && userFromList.getUsername().equals(authenticationRequest.getUsername())){
                Session newSession = new Session(userFromList.getId(), UUID.randomUUID(), tokenInfo[1], tokenInfo[0]);
                String info = "IP: " + request.getRemoteAddr();
                newSession.setSessionInfo(info);
                sessionRepository.save(newSession);
                activityRepository.save(new Activity(userFromList.getId(), System.currentTimeMillis() / 1000L, "Logged in.", true));
                activityRepository.save(new Activity(userFromList.getId(), System.currentTimeMillis() / 1000L, "Created new session: " + newSession.getSessionId(), true));
                response = new JwtResponse(tokenInfo[0], userFromList.getUsername(), userFromList.getFirstName(), userFromList.getId(), userFromList.getPermissions());
            }
        }
        if(response == null){
            System.out.println("returned just token: "+tokenInfo[0]);
            return ResponseEntity.ok(new JwtResponse(tokenInfo[0]));
        }
        else{
            System.out.println("returned token for " + response.getName() + ": " + response.getToken());
            return ResponseEntity.ok(response);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            List<User> users = userRepository.findAll();
            String passHash = null;
            for (User user : users){
                if(user.getUsername().equals(username)) {
                    passHash = DigestUtils.sha256Hex(user.getId() + password);
                    break;
                }
            }
            if(passHash != null)
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, passHash));
            else throw new Exception("USER_DOES_NOT_EXIST");
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            List<User> users = userRepository.findAll();
            for (User user : users){
                if(user.getUsername().equals(username)) {
                    activityRepository.save(new Activity(user.getId(), System.currentTimeMillis() / 1000L, "Login attempt. [INVALID CREDENTIALS]", false));
                    break;
                }
            }
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/api/v1/session/destroy", method = RequestMethod.DELETE)
    public ResponseEntity<?> destroySession(@RequestHeader("Authorization") String authHeader) throws ResourceNotFoundException {
        List<Session> allSessions = sessionRepository.findAll();
        for (Session sessionFromList : allSessions) {
            if (authHeader.contains(sessionFromList.getSessionToken())){
                sessionRepository.delete(sessionFromList);
                activityRepository.save(new Activity(sessionFromList.getUserId(), System.currentTimeMillis() / 1000L, "Destroyed session " + sessionFromList.getSessionId(), true));
                return ResponseEntity.ok("Session destroyed.");
            }
        }
        throw new ResourceNotFoundException("Session does not exist!");
    }

}
