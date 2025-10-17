package com.example.fantasticosback.mapper;

import com.example.fantasticosback.dto.response.DeanDTO;
import com.example.fantasticosback.model.Document.Dean;
import org.springframework.stereotype.Component;

@Component
public class DeanMapper {

    public DeanDTO fromDocument(Dean dean) {
        if (dean == null) return null;
        return new DeanDTO(
                dean.getId(),
                dean.getName(),
                dean.getFaculty()
        );
    }

    public Dean fromDTO(DeanDTO dto) {
        if (dto == null) return null;
        return new Dean(
                dto.getId(),
                dto.getName(),
                dto.getFaculty()
        );
    }
}