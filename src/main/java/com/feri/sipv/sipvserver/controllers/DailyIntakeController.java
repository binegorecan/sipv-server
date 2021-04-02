package com.feri.sipv.sipvserver.controllers;

import com.feri.sipv.sipvserver.comparators.SortFoodsByName;
import com.feri.sipv.sipvserver.comparators.SortIntakesByDay;
import com.feri.sipv.sipvserver.exceptions.RejectedException;
import com.feri.sipv.sipvserver.exceptions.ResourceNotFoundException;
import com.feri.sipv.sipvserver.models.Activity;
import com.feri.sipv.sipvserver.models.Session;
import com.feri.sipv.sipvserver.models.User;
import com.feri.sipv.sipvserver.models.foods.Food;
import com.feri.sipv.sipvserver.models.intakes.DailyIntake;
import com.feri.sipv.sipvserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DailyIntakeController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DailyIntakeRepository dailyIntakeRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/intake/{day}/{month}/{year}")
    public ResponseEntity<?> getDailyIntake(@PathVariable("day") int day, @PathVariable("month") int month, @PathVariable("year") int year, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        List<DailyIntake> intakes = dailyIntakeRepository.findAll();
        List<DailyIntake> usersIntakes = new ArrayList<>();
        for(DailyIntake intake : intakes){
            if (intake.getUserId() != null && intake.getUserId().equals(user.getId())){
                if(intake.getDay() == day && intake.getMonth() == month && intake.getYear() == year){
                    usersIntakes.add(intake);
                }
            }
        }

        usersIntakes.sort(new SortIntakesByDay());

        return ResponseEntity.ok(usersIntakes);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/intake/{day}/{month}/{year}/{foodId}")
    public ResponseEntity<?> addDailyIntake(@PathVariable("day") int day, @PathVariable("month") int month, @PathVariable("year") int year, @PathVariable("foodId") UUID foodId, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");

        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("No such food!"));

        DailyIntake intake = new DailyIntake(user.getId(), food.getId());
        intake.setDay(day);
        intake.setMonth(month);
        intake.setYear(year);

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] added a new intake[" + intake.getId() + "]", true ));

        return ResponseEntity.ok(intake);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/intake/{id}")
    public ResponseEntity<?> addDailyIntake(@PathVariable("id") UUID id, @RequestHeader(name="Authorization") String token) throws ResourceNotFoundException {

        User user = null;
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions){
            if(session.getSessionToken().equals(token.substring(7))){
                user = userRepository.findById(session.getUserId()).orElseThrow(() -> new ResourceNotFoundException("No such user!"));
            }
        }
        if (user == null) throw new ResourceNotFoundException("Could not find profile!");


        DailyIntake intake = dailyIntakeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No such intake!"));
        dailyIntakeRepository.delete(intake);

        activityRepository.save(new Activity(user.getId(), "User " + user.getUsername() + "[ " + user.getId() + "] deleted an intake[" + intake.getId() + "]", true ));

        return ResponseEntity.ok("Intake was deleted");
    }
}
