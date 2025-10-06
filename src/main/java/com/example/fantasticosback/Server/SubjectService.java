package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Repository.SubjectRepository;
import com.example.fantasticosback.Model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject save(Subject subject) { return subjectRepository.save(subject);}

    public List<Subject> findAll() {return subjectRepository.findAll();}

    public Subject findById(String id) {return subjectRepository.findById(id).orElse(null);}

    public Subject update(Subject subject) {return subjectRepository.save(subject);}

    public void delete(String id) {subjectRepository.deleteById(id);}

    public List<Subject> findByName(String name) {return subjectRepository.findByName(name);}

    public List<Subject> findBySemester(int semester) {return subjectRepository.findBySemester(semester);}

    public List<Subject> findByCredits(int credits) {return subjectRepository.findByCredits(credits);}

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
                dto.getId(),
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
