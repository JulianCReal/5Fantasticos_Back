package com.example.fantasticosback.Server;

import com.example.fantasticosback.Exception.ResourceNotFoundException;
import com.example.fantasticosback.Model.Document.Request;
import com.example.fantasticosback.Model.Document.Group;
import com.example.fantasticosback.Persistence.Repository.RequestRepository;
import com.example.fantasticosback.Persistence.Service.RequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private Group originGroup;
    @Mock
    private Group destinationGroup;
    @InjectMocks
    private RequestService requestService;

    private Request getRequestDummy() {
        return new Request("1", originGroup, destinationGroup, "GROUP_CHANGE", "Change group request", new Date(), "E001");
    }

    @Test
    void testSave() {
        Request request = getRequestDummy();
        when(requestRepository.save(request)).thenReturn(request);
        assertEquals(request, requestService.save(request));
    }

    @Test
    void testFindAll() {
        List<Request> list = List.of(getRequestDummy());
        when(requestRepository.findAll()).thenReturn(list);
        assertEquals(list, requestService.findAll());
    }

    @Test
    void testFindById() {
        Request request = getRequestDummy();
        when(requestRepository.findById("1")).thenReturn(Optional.of(request));
        assertEquals(request, requestService.findById("1"));
        when(requestRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> requestService.findById("2"));
    }

    @Test
    void testUpdate() {
        Request request = getRequestDummy();
        when(requestRepository.findById(request.getRequestId())).thenReturn(Optional.of(request));
        when(requestRepository.save(request)).thenReturn(request);
        assertEquals(request, requestService.update(request));
    }

    @Test
    void testDelete() {
        // Caso exitoso: el Request existe
        when(requestRepository.findById("1")).thenReturn(Optional.of(getRequestDummy()));
        requestService.delete("1");
        verify(requestRepository).deleteById("1");

        // Caso de excepción: el Request no existe
        when(requestRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> requestService.delete("2"));
    }
}
*/