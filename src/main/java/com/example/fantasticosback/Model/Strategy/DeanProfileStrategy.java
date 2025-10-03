package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.DeanOfficeService;
import com.example.fantasticosback.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeanProfileStrategy implements ProfileStrategy {

    @Autowired
    private DeanOfficeService deanOfficeService;

    @Override
    public Object obtainProfile(String perfilId) {
        return deanOfficeService.obtenerPorId(perfilId);
    }

    @Override
    public Role getRol() {
        return Role.DECANO;
    }
}