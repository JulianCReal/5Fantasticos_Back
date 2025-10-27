package com.example.fantasticosback.model.Strategy;

import com.example.fantasticosback.service.DeanOfficeService;
import com.example.fantasticosback.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeanProfileStrategy implements ProfileStrategy {
    @Autowired
    private DeanOfficeService deanOfficeService;

    @Override
    public Object getProfile(String profileId) {
        return deanOfficeService.findById(profileId);
    }

    @Override
    public Role getRole() {
        return Role.DEAN;
    }
}