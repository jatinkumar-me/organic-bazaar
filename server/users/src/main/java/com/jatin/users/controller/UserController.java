package com.jatin.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jatin.users.model.AuthenticationRequest;
import com.jatin.users.model.AuthenticationResponse;
import com.jatin.users.model.User;
import com.jatin.users.service.UserService;

/**
 * UserController
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.register(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authResponse = new AuthenticationResponse(userService.verify(authenticationRequest));
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<User> getUserById(@PathVariable Long id) {
    //     User user = userService.getUserById(id);
    //     return ResponseEntity.ok(user);
    // }
    //
    // @PutMapping("/{id}")
    // public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    //     User updatedUser = userService.updateUser(id, userDetails);
    //     return ResponseEntity.ok(updatedUser);
    // }
    //
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    //     userService.deleteUser(id);
    //     return ResponseEntity.noContent().build();
    // }
}
