package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.repository.TeacherRepository;
import com.example.fantasticosback.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public TeacherDTO save(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.fromDTO(teacherDTO);
        validateTeacher(teacher);

        Teacher existing = teacherRepository.findByDocument(teacher.getDocument());
        if (existing != null) {
            throw new BusinessValidationException("A teacher already exists with document: " + teacher.getDocument());
        }

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.fromDocument(saved);
    }

    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::fromDocument)
                .collect(Collectors.toList());
    }

    public TeacherDTO findById(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return teacherMapper.fromDocument(teacher);
    }

    public TeacherDTO update(String id, TeacherDTO teacherDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        Teacher updated = teacherMapper.fromDTO(teacherDTO);
        validateTeacher(updated);

        if (!existing.getDocument().equals(updated.getDocument())) {
            Teacher byDocument = teacherRepository.findByDocument(updated.getDocument());
            if (byDocument != null) {
                throw new BusinessValidationException("A teacher already exists with document: " + updated.getDocument());
            }
        }

        existing.setName(updated.getName());
        existing.setLastName(updated.getLastName());
        existing.setDocument(updated.getDocument());
        existing.setDepartment(updated.getDepartment());

        Teacher saved = teacherRepository.save(existing);
        return teacherMapper.fromDocument(saved);
    }

    public TeacherDTO patch(String id, TeacherDTO teacherDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        if (teacherDTO.getName() != null) existing.setName(teacherDTO.getName());
        if (teacherDTO.getLastName() != null) existing.setLastName(teacherDTO.getLastName());
        if (teacherDTO.getDocument() != null && !teacherDTO.getDocument().equals(existing.getDocument())) {
            Teacher byDocument = teacherRepository.findByDocument(teacherDTO.getDocument());
            if (byDocument != null) {
                throw new BusinessValidationException("A teacher already exists with document: " + teacherDTO.getDocument());
            }
            existing.setDocument(teacherDTO.getDocument());
        }
        if (teacherDTO.getDepartment() != null) existing.setDepartment(teacherDTO.getDepartment());

        validateTeacher(existing);

        Teacher saved = teacherRepository.save(existing);
        return teacherMapper.fromDocument(saved);
    }

    public void delete(String id) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        teacherRepository.deleteById(id);
    }

    public List<TeacherDTO> findByDepartment(String department) {
        List<Teacher> teachers = teacherRepository.findByDepartment(department);
        if (teachers.isEmpty()) {
            throw new ResourceNotFoundException("Teacher", "department", department);
        }
        return teachers.stream()
                .map(teacherMapper::fromDocument)
                .collect(Collectors.toList());
    }

    public List<TeacherDTO> findByName(String name) {
        List<Teacher> teachers = teacherRepository.findByName(name);
        if (teachers.isEmpty()) {
            throw new ResourceNotFoundException("Teacher", "name", name);
        }
        return teachers.stream()
                .map(teacherMapper::fromDocument)
                .collect(Collectors.toList());
    }

    public List<TeacherDTO> findByLastName(String lastName) {
        List<Teacher> teachers = teacherRepository.findByLastName(lastName);
        if (teachers.isEmpty()) {
            throw new ResourceNotFoundException("Teacher", "lastName", lastName);
        }
        return teachers.stream()
                .map(teacherMapper::fromDocument)
                .collect(Collectors.toList());
    }

    private void validateTeacher(Teacher teacher) {
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new BusinessValidationException("Teacher name cannot be null or empty");
        }
        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty()) {
            throw new BusinessValidationException("Teacher last name cannot be null or empty");
        }
        if (teacher.getDocument() == null || teacher.getDocument().trim().isEmpty()) {
            throw new BusinessValidationException("Teacher document cannot be null or empty");
        }
    }
}
