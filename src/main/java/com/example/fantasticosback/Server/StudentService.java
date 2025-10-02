package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public StudentDTO convertirAEstudianteDTO(Estudiante estudiante) {
        return new StudentDTO(
                estudiante.getIdEstudiante(),
                estudiante.getNombre(),
                estudiante.getCarrera(),
                estudiante.getSemestre()
        );
    }
    public List<StudentDTO> convertirLista(List<Estudiante> estudiantes) {
        return estudiantes.stream().map(this::convertirAEstudianteDTO).collect(Collectors.toList());
    }
    public Estudiante convertirADominio(StudentDTO dto) {
        return new Estudiante(
                dto.getNombre(),
                "",
                0,
                dto.getCarrera(),
                "",
                dto.getId(),
                dto.getSemestre(),
                null
        );
    }
}
