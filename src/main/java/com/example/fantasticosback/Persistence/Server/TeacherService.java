package com.example.fantasticosback.Persistence.Server;

import com.example.fantasticosback.Dtos.TeacherDTO;
import com.example.fantasticosback.Persistence.Repository.TeacherRepository;
import com.example.fantasticosback.Model.Entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findById(String id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Teacher update(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void delete(String id) {
        teacherRepository.deleteById(id);
    }

    public List<Teacher> findByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    public List<Teacher> findByName(String name) {
        return teacherRepository.findByName(name);
    }

    public List<Teacher> findByLastName(String lastName) {
        return teacherRepository.findByLastName(lastName);
    }

    public TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getLastName(),
                teacher.getDocument(),
                teacher.getDepartment()
        );
    }

    public Teacher fromDTO(TeacherDTO dto) {
        Teacher teacher = new Teacher(
                dto.getName(),
                dto.getLastName(),
                dto.getDocument(),
                dto.getDepartment()
        );
        teacher.setId(dto.getId());
        return teacher;
    }

    public List<TeacherDTO> toDTOList(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).toList();
    }
}
