package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.DeanDTO;
import com.example.fantasticosback.model.Document.Dean;
import com.example.fantasticosback.repository.DeanOfficeRepository;
import com.example.fantasticosback.repository.DeanRepository;
import com.example.fantasticosback.mapper.DeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeanService {

    private final DeanRepository deanRepository;
    private final DeanOfficeRepository deanOfficeRepository;
    private final DeanMapper deanMapper;

    public DeanService(DeanRepository deanRepository,
                       DeanOfficeRepository deanOfficeRepository,
                       DeanMapper deanMapper) {
        this.deanRepository = deanRepository;
        this.deanOfficeRepository = deanOfficeRepository;
        this.deanMapper = deanMapper;
    }

    public List<DeanDTO> getAll() {
        return deanRepository.findAll()
                .stream()
                .map(deanMapper::fromDocument)
                .collect(Collectors.toList());
    }

    public DeanDTO getById(String id) {
        return deanRepository.findById(id)
                .map(deanMapper::fromDocument)
                .orElseThrow(() -> new RuntimeException("Dean not found"));
    }

    public DeanDTO create(DeanDTO dto) {
        Dean dean = deanMapper.fromDTO(dto);
        Dean saved = deanRepository.save(dean);
        return deanMapper.fromDocument(saved);
    }

    public DeanDTO update(String id, DeanDTO dto) {
        Dean existing = deanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dean not found"));
        existing.setName(dto.getName());
        existing.setFaculty(dto.getFaculty());
        Dean updated = deanRepository.save(existing);
        return deanMapper.fromDocument(updated);
    }

    public void delete(String id) {
        deanRepository.deleteById(id);
    }


    /*public void respondRequest(String faculty, String requestId, String response) {
        DeanOffice deanOffice = deanOfficeRepository.findByFaculty(faculty)
                .orElseThrow(() -> new RuntimeException("Dean Office not found"));

        deanOffice.respondRequest(requestId, response);
        deanOfficeRepository.save(deanOffice);
    }*/
}
