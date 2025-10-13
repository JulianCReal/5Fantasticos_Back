package com.example.fantasticosback.util;

import com.example.fantasticosback.Dtos.DeanDTO;
import com.example.fantasticosback.Model.Document.Dean;
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
