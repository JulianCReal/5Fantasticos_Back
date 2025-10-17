package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.DeanOfficeDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.repository.DeanOfficeRepository;
import com.example.fantasticosback.model.Document.DeanOffice;
import com.example.fantasticosback.mapper.DeanOfficeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeanOfficeService {

    private final DeanOfficeRepository deanOfficeRepository;
    private final DeanOfficeMapper deanOfficeMapper;

    public DeanOfficeService(DeanOfficeRepository deanOfficeRepository, DeanOfficeMapper deanOfficeMapper) {
        this.deanOfficeRepository = deanOfficeRepository;
        this.deanOfficeMapper = deanOfficeMapper;
    }


    public DeanOfficeDTO save(DeanOfficeDTO deanOfficeDTO) {
        DeanOffice deanOffice = deanOfficeMapper.fromDTO(deanOfficeDTO);
        validateDeanOffice(deanOffice);


        DeanOffice existing = deanOfficeRepository.findByFaculty(deanOffice.getFaculty());
        if (existing != null) {
            throw new BusinessValidationException("A DeanOffice already exists for faculty: " + deanOffice.getFaculty());
        }

        DeanOffice saved = deanOfficeRepository.save(deanOffice);
        return deanOfficeMapper.fromDocument(saved);
    }

    public DeanOfficeDTO patch(String id, DeanOfficeDTO deanOfficeDTO) {
        DeanOffice existing = deanOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", id));

        if (deanOfficeDTO.getFaculty() != null && !deanOfficeDTO.getFaculty().equals(existing.getFaculty())) {

            DeanOffice byFaculty = deanOfficeRepository.findByFaculty(deanOfficeDTO.getFaculty());
            if (byFaculty != null) {
                throw new BusinessValidationException("A DeanOffice already exists for faculty: " + deanOfficeDTO.getFaculty());
            }
            existing.setFaculty(deanOfficeDTO.getFaculty());
        }

        if (deanOfficeDTO.getDeanId() != null) {
            existing.setDeanId(deanOfficeDTO.getDeanId());
        }

        if (deanOfficeDTO.getStudents() != null) {
            existing.setStudents(deanOfficeDTO.getStudents());
        }

        if (deanOfficeDTO.getProfessors() != null) {
            existing.setProfessors(deanOfficeDTO.getProfessors());
        }

        if (deanOfficeDTO.getSubjects() != null) {
            existing.setSubjects(deanOfficeDTO.getSubjects());
        }

        validateDeanOffice(existing);

        DeanOffice saved = deanOfficeRepository.save(existing);
        return deanOfficeMapper.fromDocument(saved);
    }


    public List<DeanOfficeDTO> findAll() {
        return deanOfficeRepository.findAll()
                .stream()
                .map(deanOfficeMapper::fromDocument)
                .collect(Collectors.toList());
    }

    public DeanOfficeDTO findById(String id) {
        DeanOffice deanOffice = deanOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", id));
        return deanOfficeMapper.fromDocument(deanOffice);
    }


    public DeanOfficeDTO findByFaculty(String faculty) {
        DeanOffice deanOffice = deanOfficeRepository.findByFaculty(faculty);
        if (deanOffice == null) {
            throw new ResourceNotFoundException("DeanOffice", "faculty", faculty);
        }
        return deanOfficeMapper.fromDocument(deanOffice);
    }


    public DeanOfficeDTO update(String id, DeanOfficeDTO deanOfficeDTO) {
        DeanOffice existing = deanOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", id));

        DeanOffice updated = deanOfficeMapper.fromDTO(deanOfficeDTO);
        validateDeanOffice(updated);


        if (!existing.getFaculty().equals(updated.getFaculty())) {
            DeanOffice byFaculty = deanOfficeRepository.findByFaculty(updated.getFaculty());
            if (byFaculty != null) {
                throw new BusinessValidationException("A DeanOffice already exists for faculty: " + updated.getFaculty());
            }
        }

        existing.setFaculty(updated.getFaculty());
        existing.setDeanId(updated.getDeanId());
        existing.setStudents(updated.getStudents());
        existing.setProfessors(updated.getProfessors());
        existing.setSubjects(updated.getSubjects());

        DeanOffice saved = deanOfficeRepository.save(existing);
        return deanOfficeMapper.fromDocument(saved);
    }

    public void delete(String id) {
        DeanOffice existing = deanOfficeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", id));

        if (existing.getStudents() != null && !existing.getStudents().isEmpty()) {
            throw new BusinessValidationException("Cannot delete DeanOffice with assigned students. Remove them first.");
        }

        deanOfficeRepository.deleteById(id);
    }



    public DeanOfficeDTO addStudent(String deanOfficeId, Student student) {
        DeanOffice deanOffice = deanOfficeRepository.findById(deanOfficeId)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", deanOfficeId));

        deanOffice.addStudentId(student.getStudentId());
        DeanOffice updated = deanOfficeRepository.save(deanOffice);
        return deanOfficeMapper.fromDocument(updated);
    }


    public List<String> getStudents(String deanOfficeId) {
        DeanOffice deanOffice = deanOfficeRepository.findById(deanOfficeId)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", deanOfficeId));
        return deanOffice.getStudents();
    }
    public List<String> getStudentsByFaculty(String faculty) {
        DeanOffice deanOffice = deanOfficeRepository.findByFaculty(faculty);
        if (deanOffice == null) {
            throw new ResourceNotFoundException("DeanOffice", "faculty", faculty);
        }
        return deanOffice.getStudents();
    }


    private void validateDeanOffice(DeanOffice deanOffice) {
        if (deanOffice.getFaculty() == null || deanOffice.getFaculty().trim().isEmpty()) {
            throw new BusinessValidationException("Faculty name cannot be null or empty");
        }
    }

    public DeanOfficeDTO addRequest(String deanOfficeId, String requestId) {
        DeanOffice deanOffice = deanOfficeRepository.findById(deanOfficeId)
                .orElseThrow(() -> new ResourceNotFoundException("DeanOffice", "id", deanOfficeId));

        if (deanOffice.getRequests() == null) {
            deanOffice.setRequests(new ArrayList<>());
        }


        if (deanOffice.getRequests().contains(requestId)) {
            throw new BusinessValidationException("This request is already associated with the DeanOffice.");
        }

        deanOffice.getRequests().add(requestId);
        DeanOffice updated = deanOfficeRepository.save(deanOffice);

        return deanOfficeMapper.fromDocument(updated);
    }

}
