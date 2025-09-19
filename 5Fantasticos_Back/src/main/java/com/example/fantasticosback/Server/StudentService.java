package com.example.fantasticosback.Server;

import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.util.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Estudiante guardar(Estudiante estudiante) {
        return studentRepository.save(estudiante);
    }

    public List<Estudiante> obtenerTodos() {
        return studentRepository.findAll();
    }

    public Estudiante obtenerPorId(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Estudiante actualizar(Estudiante estudiante) {
        return studentRepository.save(estudiante); // save actualiza si el ID existe
    }

    public void eliminar(String id) {
        studentRepository.deleteById(id);
    }

    public List<Estudiante> obtenerPorCarrera(String carrera) {
        return studentRepository.findByCarrera(carrera);
    }

    public List<Estudiante> obtenerPorSemestre(int semestre) {
        return studentRepository.findBySemestre(semestre);
    }
}
