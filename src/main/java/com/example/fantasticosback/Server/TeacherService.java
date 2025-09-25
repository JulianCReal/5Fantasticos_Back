package com.example.fantasticosback.Server;

import com.example.fantasticosback.Repository.TeacherRepository;
import com.example.fantasticosback.Model.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Profesor guardar(Profesor profesor) {
        return teacherRepository.save(profesor);
    }

    public List<Profesor> obtenerTodos() {
        return teacherRepository.findAll();
    }

    public Profesor obtenerPorId(String id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Profesor actualizar(Profesor profesor) {
        return teacherRepository.save(profesor);
    }

    public void eliminar(String id) {
        teacherRepository.deleteById(id);
    }

    public List<Profesor> obtenerPorDepartamento(String departamento) {
        return teacherRepository.findByDepartamento(departamento);
    }

    public List<Profesor> obtenerPorNombre(String nombre) {
        return teacherRepository.findByNombre(nombre);
    }

    public List<Profesor> obtenerPorApellido(String apellido) {
        return teacherRepository.findByApellido(apellido);
    }
}
