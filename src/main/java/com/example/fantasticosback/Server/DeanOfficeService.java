package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Model.DeanOffice;
import com.example.fantasticosback.Repository.DeanOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeanOfficeService {

    @Autowired
    private DeanOfficeRepository deanOfficeRepository;

    public DeanOffice guardar(DeanOffice deanOffice) {
        return deanOfficeRepository.save(deanOffice);
    }

    public List<DeanOffice> obtenerTodos() {
        return deanOfficeRepository.findAll();
    }

    public DeanOffice obtenerPorId(String id) {
        return deanOfficeRepository.findById(id).orElse(null);
    }

    public DeanOffice actualizar(DeanOffice deanOffice) {
        return deanOfficeRepository.save(deanOffice);
    }

    public void eliminar(String id) {
        deanOfficeRepository.deleteById(id);
    }

    public List<DeanOffice> obtenerPorFacultad(String facultad) {
        return deanOfficeRepository.findByFaculty(facultad);
    }

    public DeanOfficeDTO toDTO(DeanOffice deanOffice) {
        return new DeanOfficeDTO(
                deanOffice.getId(),
                deanOffice.getFaculty(),
                deanOffice.getEstudiantes() != null ? deanOffice.getEstudiantes() : new ArrayList<>()
        );
    }

    public DeanOffice fromDTO(DeanOfficeDTO dto) {
        DeanOffice deanOffice = new DeanOffice(dto.getId(), dto.getFaculty());
        dto.getStudents().forEach(deanOffice::addEstudiante);
        return deanOffice;
    }

    public List<DeanOfficeDTO> toDTOList(List<DeanOffice> deanOffices) {
        return deanOffices.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
