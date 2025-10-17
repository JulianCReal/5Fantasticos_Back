package com.example.fantasticosback.mapper;

import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.model.Document.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher fromDTO(TeacherDTO dto) {
        if (dto == null) {
            return null;
        }

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setName(dto.getName());
        teacher.setLastName(dto.getLastName());
        teacher.setDocument(dto.getDocument());
        teacher.setDepartment(dto.getDepartment());

        return teacher;
    }

    public TeacherDTO fromDocument(Teacher document) {
        if (document == null) {
            return null;
        }

        TeacherDTO dto = new TeacherDTO(
                document.getId(),
                document.getName(),
                document.getLastName(),
                document.getDocument(),
                document.getDepartment()
        );

        return dto;
    }

    public Teacher toDomain(Teacher teacherDocument) {
        if (teacherDocument == null) return null;

        Teacher domainTeacher = new Teacher();
        domainTeacher.setId(teacherDocument.getId());
        domainTeacher.setName(teacherDocument.getName());
        domainTeacher.setLastName(teacherDocument.getLastName());
        domainTeacher.setDocument(teacherDocument.getDocument());
        domainTeacher.setDepartment(teacherDocument.getDepartment());
        return domainTeacher;
    }
}
