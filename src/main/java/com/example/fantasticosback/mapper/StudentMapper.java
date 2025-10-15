package com.example.fantasticosback.mapper;

import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.model.Document.Student;
import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getCareer(),
                student.getSemester()
        );
    }

    public static List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }

    public static Student toDomain(StudentDTO dto) {
        return new Student(
                dto.getName(),
                "",
                0,
                dto.getCareer(),
                "",
                dto.getId(),
                dto.getSemester(),
                null
        );
    }
}

