package com.example.fantasticosback.util;



import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Model.Document.DeanOffice;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class DeanOfficeMapper {


    public DeanOffice fromDTO(DeanOfficeDTO dto) {
        if (dto == null) {
            return null;
        }

        DeanOffice deanOffice = new DeanOffice();
        deanOffice.setId(dto.getId());
        deanOffice.setFaculty(dto.getFaculty());
        deanOffice.setDeanName(dto.getDeanName());

        if (dto.getStudents() != null) {
            deanOffice.setStudents(dto.getStudents().stream().collect(Collectors.toList()));
        }
        if (dto.getProfessors() != null) {
            deanOffice.setProfessors(dto.getProfessors().stream().collect(Collectors.toList()));
        }
        if (dto.getSubjects() != null) {
            deanOffice.setSubjects(dto.getSubjects().stream().collect(Collectors.toList()));
        }

        return deanOffice;
    }


    public DeanOfficeDTO fromDocument(DeanOffice document) {
        if (document == null) {
            return null;
        }

        DeanOfficeDTO dto = new DeanOfficeDTO();
        dto.setId(document.getId());
        dto.setFaculty(document.getFaculty());
        dto.setDeanName(document.getDeanName());

        if (document.getStudents() != null) {
            dto.setStudents(document.getStudents().stream().collect(Collectors.toList()));
        }
        if (document.getProfessors() != null) {
            dto.setProfessors(document.getProfessors().stream().collect(Collectors.toList()));
        }
        if (document.getSubjects() != null) {
            dto.setSubjects(document.getSubjects().stream().collect(Collectors.toList()));
        }

        return dto;
    }
}
