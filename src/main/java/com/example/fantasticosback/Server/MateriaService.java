package com.example.fantasticosback.Server;

import com.example.fantasticosback.Repository.MateriaRepository;
import com.example.fantasticosback.Model.Materia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    public Materia guardar(Materia materia) { return materiaRepository.save(materia);}

    public List<Materia> obtenerTodos() {return materiaRepository.findAll();}

    public Materia obtenerPorId(String id) {return materiaRepository.findById(id).orElse(null);}

    public Materia actualizar(Materia materia) {return materiaRepository.save(materia);}

    public void eliminar(String id) {materiaRepository.deleteById(id);}

    public List<Materia> obtenerPorNombre(String nombre) {return materiaRepository.findByNombre(nombre);}

    public List<Materia> obtenerPorSemestre(int semestre) {return materiaRepository.findBySemestre(semestre);}

    public List<Materia> obtenerPorCreditos(int creditos) {return materiaRepository.findByCreditos(creditos);}
}
