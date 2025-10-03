package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.TeacherDTO;
import com.example.fantasticosback.Repository.ProfessorRepository;
import com.example.fantasticosback.Model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Professor guardar(Professor professor) {
        return professorRepository.save(professor);
    }

    public List<Professor> obtenerTodos() {
        return professorRepository.findAll();
    }

    public Professor obtenerPorId(String id) {
        return professorRepository.findById(id).orElse(null);
    }

    public Professor actualizar(Professor professor) {
        return professorRepository.save(professor);
    }

    public void eliminar(String id) {
        professorRepository.deleteById(id);
    }

    public List<Professor> obtenerPorDepartamento(String departamento) {
        return professorRepository.findByDepartment(departamento);
    }

    public List<Professor> obtenerPorNombre(String nombre) {
        return professorRepository.findByFirstName(nombre);
    }

    public List<Professor> obtenerPorApellido(String apellido) {
        return professorRepository.findByLastName(apellido);
    }
    public TeacherDTO toDTO(Professor professor) {
        return new TeacherDTO(
                professor.getId(),
                professor.getNombre(),
                professor.getLastName(),
                professor.getDocument(),
                professor.getDepartament()
        );
    }

    public Professor fromDTO(TeacherDTO dto) {
        Professor professor = new Professor(
                dto.getName(),
                dto.getLastName(),
                dto.getDocument(),
                dto.getDepartment()
        );
        professor.setId(dto.getId());
        return professor;
    }

    public List<TeacherDTO> toDTOList(List<Professor> profesores) {
        return profesores.stream().map(this::toDTO).toList();
    }

}
