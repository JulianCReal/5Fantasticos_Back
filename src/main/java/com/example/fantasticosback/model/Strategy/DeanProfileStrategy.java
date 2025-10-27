package com.example.fantasticosback.model.Strategy;

import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.service.DeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeanProfileStrategy implements ProfileStrategy {
    @Autowired
    private DeanService deanService;

    @Override
    public Object getProfile(String profileId) {
        return deanService.getById(profileId);
    }

    @Override
    public Role getRole() {
        return Role.DEAN;
    }
}