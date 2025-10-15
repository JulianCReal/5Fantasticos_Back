package com.example.fantasticosback.Mapper;

import com.example.fantasticosback.Dto.Response.StudentDTO;
import com.example.fantasticosback.Model.Document.Student;
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

