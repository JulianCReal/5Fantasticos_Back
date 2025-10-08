package com.example.fantasticosback.Persistence.Server;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Exception.ResourceNotFoundException;
import com.example.fantasticosback.Exception.BusinessValidationException;
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
        if (deanOffice.getFaculty() == null || deanOffice.getFaculty().trim().isEmpty()) {
            throw new BusinessValidationException("Faculty name cannot be null or empty");
        }

        List<DeanOffice> existingByFaculty = deanOfficeRepository.findByFaculty(deanOffice.getFaculty());
        if (!existingByFaculty.isEmpty()) {
            throw new BusinessValidationException("A Dean Office already exists for faculty: " + deanOffice.getFaculty());
        }

        return deanOfficeRepository.save(deanOffice);
    }

    public List<DeanOffice> findAll() {
        return deanOfficeRepository.findAll();
    }

    public DeanOffice findById(String id) {
        return deanOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dean Office", "id", id));
    }

    public DeanOffice update(String id, DeanOffice deanOffice) {
        // Verificar que el dean office existe antes de actualizar
        DeanOffice existing = findById(id);

        // Validar que la facultad no esté vacía o nula
        if (deanOffice.getFaculty() == null || deanOffice.getFaculty().trim().isEmpty()) {
            throw new BusinessValidationException("Faculty name cannot be null or empty");
        }

        // Validar que no se cambie a una facultad que ya tiene dean office (excepto si es la misma)
        if (!existing.getFaculty().equals(deanOffice.getFaculty())) {
            List<DeanOffice> existingByFaculty = deanOfficeRepository.findByFaculty(deanOffice.getFaculty());
            if (!existingByFaculty.isEmpty()) {
                throw new BusinessValidationException("A Dean Office already exists for faculty: " + deanOffice.getFaculty());
            }
        }

        // Mantener el ID original
        deanOffice.setId(id);

        return deanOfficeRepository.save(deanOffice);
    }

    public void delete(String id) {
        // Verificar que el dean office existe antes de eliminar
        DeanOffice existing = findById(id);

        // Validar que no tenga estudiantes asignados antes de eliminar
        if (existing.getStudents() != null && !existing.getStudents().isEmpty()) {
            throw new BusinessValidationException("Cannot delete Dean Office with assigned students. Please reassign or remove students first.");
        }

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
