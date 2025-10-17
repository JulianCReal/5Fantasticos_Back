package com.example.fantasticosback.model.Strategy;

import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentProfileStrategy implements ProfileStrategy {

    @Autowired
    private StudentService studentService;

    @Override
    public Object getProfile(String profileId) {
        return studentService.findById(profileId);
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }
}
