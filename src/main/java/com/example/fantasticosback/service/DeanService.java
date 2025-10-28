package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.DeanDTO;
import com.example.fantasticosback.enums.RequestPriority;
import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.Dean;
import com.example.fantasticosback.model.Document.DeanOffice;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.repository.DeanOfficeRepository;
import com.example.fantasticosback.repository.DeanRepository;
import com.example.fantasticosback.mapper.DeanMapper;
import com.example.fantasticosback.repository.StudentRepository;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Service;
import com.example.fantasticosback.service.RequestService;
import com.example.fantasticosback.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeanService {

    private final DeanRepository deanRepository;
    private final DeanOfficeRepository deanOfficeRepository;
    private final DeanMapper deanMapper;
    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final StudentRepository studentRepository;
    public DeanService(DeanRepository deanRepository,
                       DeanOfficeRepository deanOfficeRepository,
                       DeanMapper deanMapper, RequestService requestService, RequestRepository requestRepository, StudentRepository studentRepository) {
        this.deanRepository = deanRepository;
        this.deanOfficeRepository = deanOfficeRepository;
        this.deanMapper = deanMapper;
        this.requestService = requestService;
        this.requestRepository = requestRepository;
        this.studentRepository = studentRepository;
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




    public Request resolveRequest(String deanId, String requestId, String responseMessage, Boolean status) {

        Request request = requestService.findById(requestId);


        DeanOffice deanOffice = deanOfficeRepository.findByDeanId(deanId);
        if (deanOffice == null) {
            throw new BusinessValidationException("This dean does not manage any DeanOffice.");
        }

        if (!request.getDeanOffice().equals(deanOffice.getFaculty())) {
            throw new BusinessValidationException("Dean cannot resolve requests from another DeanOffice.");
        }

        requestService.answerRequest(request, responseMessage, status, Role.DEAN);

        return request;
    }
    public List<String> getRequestsByDean(String deanId) {
        DeanOffice deanOffice = deanOfficeRepository.findByDeanId(deanId);
        if (deanOffice == null) {
            throw new RuntimeException("Dean not assigned to any DeanOffice");
        }


        return deanOffice.getRequests();
    }
    public List<Student> getStudentsByDean(String deanId) {
        DeanOffice deanOffice = deanOfficeRepository.findByDeanId(deanId);
        if (deanOffice == null) {
            throw new BusinessValidationException("Dean not assigned to any DeanOffice");
        }

        List<String> studentIds = deanOffice.getStudents();
        List<Student> students = new ArrayList<>();

        for (String id : studentIds) {
            studentRepository.findById(id).ifPresent(students::add);
        }

        return students;
    }
    public List<Request> getRequestsByDeanAndPriority(String deanId, String priority) {

        DeanOffice deanOffice = deanOfficeRepository.findByDeanId(deanId);
       


        RequestPriority requestPriority = null;
       


        List<String> requestIds = deanOffice.getRequests();
        List<Request> filteredRequests = new ArrayList<>();

        for (String requestId : requestIds) {
            requestRepository.findById(requestId).ifPresent(req -> {
                if (req.getRequestPriority() == requestPriority) {
                    filteredRequests.add(req);
                }
            });
        }

        return filteredRequests;
    }






}
