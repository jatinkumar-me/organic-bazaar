package com.jatin.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jatin.users.model.AuthenticationRequest;
import com.jatin.users.model.User;
import com.jatin.users.model.VerifyTokenResponse;
import com.jatin.users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public String verify(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Access denied");
        }
        return jwtService.generateToken(authenticationRequest.getEmail());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public VerifyTokenResponse validateToken(String token) {
        String email = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(email);
        VerifyTokenResponse resp = new VerifyTokenResponse();
        resp.setToken(token);
        resp.setUserId(user.getUserId());
        return resp;
    }
}
