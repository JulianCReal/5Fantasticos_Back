package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
}