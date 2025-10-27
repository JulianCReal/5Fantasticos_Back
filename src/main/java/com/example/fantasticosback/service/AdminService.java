package com.example.fantasticosback.service;


import com.example.fantasticosback.model.Document.Admin;
import com.example.fantasticosback.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findById(String id){
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
    }
}
