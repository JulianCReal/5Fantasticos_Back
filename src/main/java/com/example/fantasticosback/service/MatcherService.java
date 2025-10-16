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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        ProfileStrategy strategy = strategyFactory.getStrategy(user.getRole());
        return strategy.getProfile(user.getProfileId());
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
