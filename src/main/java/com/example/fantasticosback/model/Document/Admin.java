package com.example.fantasticosback.model.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Admins")
public class Admin {
    @Id
    private String id;
    private String email;
    private String name;
}