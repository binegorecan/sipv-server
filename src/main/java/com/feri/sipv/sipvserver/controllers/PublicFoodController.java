package com.feri.sipv.sipvserver.controllers;

import com.feri.sipv.sipvserver.comparators.SortFoodsByName;
import com.feri.sipv.sipvserver.enums.FoodType;
import com.feri.sipv.sipvserver.exceptions.RejectedException;
import com.feri.sipv.sipvserver.exceptions.ResourceNotFoundException;
import com.feri.sipv.sipvserver.models.Activity;
import com.feri.sipv.sipvserver.models.Session;
import com.feri.sipv.sipvserver.models.User;
import com.feri.sipv.sipvserver.models.foods.Food;
import com.feri.sipv.sipvserver.repositories.ActivityRepository;
import com.feri.sipv.sipvserver.repositories.FoodRepository;
import com.feri.sipv.sipvserver.repositories.SessionRepository;
import com.feri.sipv.sipvserver.repositories.UserRepository;
import com.feri.sipv.sipvserver.services.FoodTypeChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PublicFoodController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FoodTypeChecker foodTypeChecker;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/foods/public")
    public ResponseEntity<?> getPublicFoods(@RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        List<Food> foods = foodRepository.findAll();
        List<Food> publicFoods = new ArrayList<>();
        for(Food food : foods){
            if (food.getOwner() == null){
                publicFoods.add(food);
            }
        }

        publicFoods.sort(new SortFoodsByName());

        return ResponseEntity.ok(publicFoods);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/foods/public")
    public ResponseEntity<?> addPublicFood(@RequestBody Food food, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }

        if (user == null) throw new ResourceNotFoundException("Could not find profile!");
        if (food == null) throw new ResourceNotFoundException("No food was posted!");
        if (food.getName() == null || food.getName().equals("")) throw new ResourceNotFoundException("Food must have a name!");
        if (food.getKcal() == null || food.getKcal() == 0.0) throw new ResourceNotFoundException("Food must have calories");

        food.setOwner(null);
        food.setId(UUID.randomUUID());
        if(food.getFoodType() == null){
            food.setFoodType(FoodType.NOT_SPECIFIED);
        }
        else {
            if(!foodTypeChecker.isValid(food.getFoodType())){
                throw new RejectedException("Unknown food type.");
            }
        }

        Food savedFood = foodRepository.save(food);

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] added PUBLIC food[" + food.getId() + "]", true ));

        return ResponseEntity.ok(savedFood);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/foods/public")
    public ResponseEntity<?> updatePublicFood(@RequestBody Food food, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }

        if (user == null) throw new ResourceNotFoundException("Could not find profile!");
        if (food == null) throw new ResourceNotFoundException("No food was posted!");
        if (food.getName() == null || food.getName().equals("")) throw new RejectedException("Food must have a name!");
        if (food.getKcal() == null || food.getKcal() == 0.0) throw new RejectedException("Food must have calories");
        if (food.getId() == null) throw new ResourceNotFoundException("Food must have an ID for it to be updated");

        food.setOwner(null);
        if(food.getFoodType() == null){
            food.setFoodType(FoodType.NOT_SPECIFIED);
        }
        else {
            if(!foodTypeChecker.isValid(food.getFoodType())){
                throw new RejectedException("Unknown food type.");
            }
        }

        Food savedFood = foodRepository.save(food);

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] updated PUBLIC food[" + food.getId() + "]", true));

        return ResponseEntity.ok(savedFood);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/foods/public/{id}")
    public ResponseEntity<?> deletePublicFood(@PathVariable("id") UUID foodId, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        Food foodToDelete = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("No such food!"));

        if (foodToDelete.getOwner() == null){
            foodRepository.delete(foodToDelete);
        }
        else {
            throw new RejectedException("Illegal action! [Food must be public to be deletable by this endpoint]");
        }

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] deleted food[" + foodId + "]", true));

        return ResponseEntity.ok("Food was deleted");
    }
}
