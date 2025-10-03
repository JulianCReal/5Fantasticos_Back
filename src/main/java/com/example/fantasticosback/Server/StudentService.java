package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Model.Student;
import com.example.fantasticosback.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student guardar(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> obtenerTodos() {
        return studentRepository.findAll();
    }

    public Student obtenerPorId(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student actualizar(Student student) {
        return studentRepository.save(student); // save actualiza si el ID existe
    }

    public void eliminar(String id) {
        studentRepository.deleteById(id);
    }

    public List<Student> obtenerPorCarrera(String carrera) {
        return studentRepository.findByDegreeProgram(carrera);
    }

    public List<Student> obtenerPorSemestre(int semestre) {
        return studentRepository.findBySemester(semestre);
    }

    public StudentDTO convertirAEstudianteDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getNombre(),
                student.getDegreeProgram(),
                student.getSemester()
        );
    }
    public List<StudentDTO> convertirLista(List<Student> students) {
        return students.stream().map(this::convertirAEstudianteDTO).collect(Collectors.toList());
    }
    public Student convertirADominio(StudentDTO dto) {
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
