package com.example.fantasticosback.util;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.logging.Logger;

@Document(collection = "Decanaturas")
public class Decanatura {

    @Id
    private String id;  // Ahora será String para que lo maneje Mongo
    private String facultad;
    private static final Logger log = Logger.getLogger(Decanatura.class.getName());

    public Decanatura(String id, String facultad) {
        this.id = id;
        this.facultad = facultad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }


}
