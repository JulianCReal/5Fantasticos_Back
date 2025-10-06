package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.TeacherService;
import com.example.fantasticosback.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherProfileStrategy implements ProfileStrategy {

    @Autowired
    private TeacherService teacherService;

    @Override
    public Object getProfile(String profileId) {
        return teacherService.findById(profileId);
    }

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }
}