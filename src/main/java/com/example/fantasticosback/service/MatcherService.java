package com.example.fantasticosback.service;


import com.example.fantasticosback.repository.UserRepository;
import com.example.fantasticosback.model.Document.User;
import com.example.fantasticosback.model.Strategy.ProfileStrategy;
import com.example.fantasticosback.model.Factory.ProfileStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatcherService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileStrategyFactory strategyFactory;

    public Object authenticateAndGetProfile(String email, String rawPassword) {
        System.out.println("Attempting login for email: " + email);
        
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            System.out.println("User not found: " + email);
            return new RuntimeException("User not found");
        });
        
        System.out.println("User found: " + user.getEmail());
        System.out.println("Stored password hash: " + user.getPassword());
        System.out.println("Raw password: " + rawPassword);
        
        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("Password matches: " + passwordMatches);
        
        if (!passwordMatches) {
            throw new RuntimeException("Incorrect password");
        }

        System.out.println("Getting profile for role: " + user.getRole());
        ProfileStrategy strategy = strategyFactory.getStrategy(user.getRole());
        return strategy.getProfile(user.getProfileId());
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
