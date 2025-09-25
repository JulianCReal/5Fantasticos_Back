package com.example.fantasticosback.Server;

import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Model.Decanatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecanaturaService {

    @Autowired
    private DecanaturaRepository decanaturaRepository;

    public Decanatura guardar(Decanatura decanatura) {
        return decanaturaRepository.save(decanatura);
    }

    public List<Decanatura> obtenerTodos() {
        return decanaturaRepository.findAll();
    }

    public Decanatura obtenerPorId(String id) {
        return decanaturaRepository.findById(id).orElse(null);
    }

    public Decanatura actualizar(Decanatura decanatura) {
        return decanaturaRepository.save(decanatura);
    }

    public void eliminar(String id) {
        decanaturaRepository.deleteById(id);
    }

    public List<Decanatura> obtenerPorFacultad(String facultad) {
        return decanaturaRepository.findByFacultad(facultad);
    }
}
