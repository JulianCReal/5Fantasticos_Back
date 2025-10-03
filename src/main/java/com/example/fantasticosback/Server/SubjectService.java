package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject guardar(Subject subject) { return subjectRepository.save(subject);}

    public List<Subject> obtenerTodos() {return subjectRepository.findAll();}

    public Subject obtenerPorId(String id) {return subjectRepository.findById(id).orElse(null);}

    public Subject actualizar(Subject subject) {return subjectRepository.save(subject);}

    public void eliminar(String id) {
        subjectRepository.deleteById(id);}

    public List<Subject> obtenerPorNombre(String nombre) {return subjectRepository.findByName(nombre);}

    public List<Subject> obtenerPorSemestre(int semestre) {return subjectRepository.findBySemester(semestre);}

    public List<Subject> obtenerPorCreditos(int creditos) {return subjectRepository.findByCredits(creditos);}

    public SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                String.valueOf(subject.getSubjectId()),
                subject.getName(),
                subject.getCredits(),
                subject.getSemester(),
                subject.getAvailableGroups()
        );
    }


    public Subject fromDTO(SubjectDTO dto) {
        Subject subject = new Subject(
                dto.getId(),   // <-- ya es String
                dto.getName(),
                dto.getCredits(),
                dto.getSemester()
        );
        subject.setAvailableGroups(dto.getAvailableGroups());
        return subject;
    }


    public List<SubjectDTO> toDTOList(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
