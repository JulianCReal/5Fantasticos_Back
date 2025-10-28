package com.example.fantasticosback.model.Strategy;

import com.example.fantasticosback.controller.AdminController;
import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProfileStrategy implements ProfileStrategy{
    @Autowired
    private AdminService adminService;

    @Override
    public Object getProfile(String profileId) {
        return adminService.findById(profileId);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}