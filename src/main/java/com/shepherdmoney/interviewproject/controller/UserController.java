package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserRepository userRepository;

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // Creating a new user
        User newUser = new User(payload.getName(), payload.getEmail());
        User saveUser = userRepository.save(newUser);
        return ResponseEntity.ok(saveUser.getId());
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // Attempt to find the user by ID
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User with id: " + userId + " does not exist");
        }
    }
}
