package com.example.fantasticosback.Persistence.Server;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Persistence.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Model.Entities.DeanOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeanOfficeService {

    @Autowired
    private DeanOfficeRepository deanOfficeRepository;

    public DeanOffice save(DeanOffice deanOffice) {
        return deanOfficeRepository.save(deanOffice);
    }

    public List<DeanOffice> findAll() {
        return deanOfficeRepository.findAll();
    }

    public DeanOffice findById(String id) {
        return deanOfficeRepository.findById(id).orElse(null);
    }

    public DeanOffice update(DeanOffice deanOffice) {
        return deanOfficeRepository.save(deanOffice);
    }

    public void delete(String id) {
        deanOfficeRepository.deleteById(id);
    }

    public List<DeanOffice> findByFaculty(String faculty) {
        return deanOfficeRepository.findByFaculty(faculty);
    }

    public DeanOfficeDTO toDTO(DeanOffice deanOffice) {
        return new DeanOfficeDTO(
                deanOffice.getId(),
                deanOffice.getFaculty(),
                deanOffice.getStudents() != null ? deanOffice.getStudents() : new ArrayList<>()
        );
    }

    public DeanOffice fromDTO(DeanOfficeDTO dto) {
        DeanOffice deanOffice = new DeanOffice(dto.getId(), dto.getFaculty());
        dto.getStudents().forEach(deanOffice::addStudent);
        return deanOffice;
    }

    public List<DeanOfficeDTO> toDTOList(List<DeanOffice> deanOffices) {
        return deanOffices.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
