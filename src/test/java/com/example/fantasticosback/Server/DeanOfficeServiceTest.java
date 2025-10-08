package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Entities.DeanOffice;
import com.example.fantasticosback.Persistence.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Persistence.Server.DeanOfficeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeanOfficeServiceTest {
    @Mock
    private DeanOfficeRepository deanOfficeRepository;
    @InjectMocks
    private DeanOfficeService deanOfficeService;

    private DeanOffice getDeanOfficeDummy() {
        return new DeanOffice("1", "Engineering");
    }

    @Test
    void testSave() {
        DeanOffice deanOffice = getDeanOfficeDummy();
        when(deanOfficeRepository.save(deanOffice)).thenReturn(deanOffice);
        assertEquals(deanOffice, deanOfficeService.save(deanOffice));
    }

    @Test
    void testFindAll() {
        List<DeanOffice> list = List.of(getDeanOfficeDummy());
        when(deanOfficeRepository.findAll()).thenReturn(list);
        assertEquals(list, deanOfficeService.findAll());
    }

    @Test
    void testFindById() {
        DeanOffice deanOffice = getDeanOfficeDummy();
        when(deanOfficeRepository.findById("1")).thenReturn(Optional.of(deanOffice));
        assertEquals(deanOffice, deanOfficeService.findById("1"));
        when(deanOfficeRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(deanOfficeService.findById("2"));
    }

    @Test
    void testUpdate() {
        DeanOffice deanOffice = getDeanOfficeDummy();
        when(deanOfficeRepository.save(deanOffice)).thenReturn(deanOffice);
        assertEquals(deanOffice, deanOfficeService.update(deanOffice));
    }

    @Test
    void testDelete() {
        deanOfficeService.delete("1");
        verify(deanOfficeRepository).deleteById("1");
    }

    @Test
    void testFindByFaculty() {
        List<DeanOffice> list = List.of(getDeanOfficeDummy());
        when(deanOfficeRepository.findByFaculty("Engineering")).thenReturn(list);
        assertEquals(list, deanOfficeService.findByFaculty("Engineering"));
    }
}
