package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReadPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
}
