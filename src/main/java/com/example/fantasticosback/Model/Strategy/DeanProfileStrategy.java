package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Persistence.Service.DeanOfficeService;
import com.example.fantasticosback.util.Enums.Role;
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
        return Role.DEAN_OFFICE;
    }
}