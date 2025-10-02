package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Model.Decanatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public DeanOfficeDTO toDTO(Decanatura decanatura) {
        return new DeanOfficeDTO(
                decanatura.getId(),
                decanatura.getFacultad(),
                decanatura.getEstudiantes() != null ? decanatura.getEstudiantes() : new ArrayList<>()
        );
    }

    public Decanatura fromDTO(DeanOfficeDTO dto) {
        Decanatura decanatura = new Decanatura(dto.getId(), dto.getFaculty());
        dto.getStudents().forEach(decanatura::addEstudiante);
        return decanatura;
    }

    public List<DeanOfficeDTO> toDTOList(List<Decanatura> decanaturas) {
        return decanaturas.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
