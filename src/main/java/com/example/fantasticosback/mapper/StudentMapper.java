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
                student.getLastName(),
                student.getDocument(),
                student.getCareer(),
                student.getCurrentSemester()
        );
    }

    public static List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }

    public static Student toDomain(StudentDTO dto) {
        if (dto == null) return null;
        Student student = new Student();
        student.setStudentId(dto.getId());
        student.setName(dto.getName());
        student.setCareer(dto.getCareer());
        student.setCurrentSemester(dto.getSemester());
        student.setLastName(dto.getLastName());
        student.setDocument(dto.getDocument());
        student.setApprovedSubjectIds(new java.util.ArrayList<>());
        student.setScheduleIds(new java.util.ArrayList<>());
        student.setRequestIds(new java.util.ArrayList<>());
        student.setAcademicTrafficLight(null);
        return student;
    }
}
