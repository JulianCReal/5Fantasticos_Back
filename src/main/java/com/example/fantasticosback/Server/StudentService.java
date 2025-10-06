package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Student student) {
        return studentRepository.save(student);
    }

    public void delete(String id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByCareer(String career) {
        return studentRepository.findByCareer(career);
    }

    public List<Student> findBySemester(int semester) {
        return studentRepository.findBySemester(semester);
    }

    public StudentDTO convertToStudentDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getCareer(),
                student.getSemester()
        );
    }

    public List<StudentDTO> convertList(List<Student> students) {
        return students.stream().map(this::convertToStudentDTO).collect(Collectors.toList());
    }

    public Student convertToDomain(StudentDTO dto) {
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
