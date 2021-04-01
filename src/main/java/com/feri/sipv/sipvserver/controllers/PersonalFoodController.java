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
public class PersonalFoodController {

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
    @GetMapping("/foods/personal")
    public ResponseEntity<?> getProfile(@RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        List<Food> foods = foodRepository.findAll();
        List<Food> usersFoods = new ArrayList<>();
        for(Food food : foods){
            if (food.getOwner() != null && food.getOwner().equals(user.getId())){
                usersFoods.add(food);
            }
        }

        usersFoods.sort(new SortFoodsByName());

        return ResponseEntity.ok(usersFoods);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/foods/personal")
    public ResponseEntity<?> addPersonalFood(@RequestBody Food food, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
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
        if (food.getOwner() != null && !food.getOwner().equals(user.getId())) throw new RejectedException("Illegal action! [Owner ID must be equal to JWT owner ID]");

        food.setOwner(user.getId());
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

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] added a new food[" + food.getId() + "]", true ));

        return ResponseEntity.ok(savedFood);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/foods/personal")
    public ResponseEntity<?> updatePersonalFood(@RequestBody Food food, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
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
        if (food.getOwner() == null) throw new RejectedException("Illegal action! [Food must be personal to be editable by this endpoint]");
        if (!food.getOwner().equals(user.getId())) throw new RejectedException("Illegal action! [Owner ID must be equal to JWT owner ID]");
        if (food.getId() == null) throw new ResourceNotFoundException("Food must have an ID for it to be updated");

        food.setOwner(user.getId());
        if(food.getFoodType() == null){
            food.setFoodType(FoodType.NOT_SPECIFIED);
        }
        else {
            if(!foodTypeChecker.isValid(food.getFoodType())){
                throw new RejectedException("Unknown food type.");
            }
        }

        Food savedFood = foodRepository.save(food);

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] updated food[" + food.getId() + "]", true));

        return ResponseEntity.ok(savedFood);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/foods/personal/{id}")
    public ResponseEntity<?> deletePersonalFood(@PathVariable("id") UUID foodId, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException, RejectedException {
        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        Food foodToDelete = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("No such food!"));

        if (foodToDelete.getOwner() != null){
            foodRepository.delete(foodToDelete);
        }
        else {
            throw new RejectedException("Illegal action! [Food must be private to be deletable by this endpoint]");
        }

        foodRepository.delete(foodToDelete);
        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] deleted food[" + foodId + "]", true));

        return ResponseEntity.ok("Food was deleted");
    }

}
