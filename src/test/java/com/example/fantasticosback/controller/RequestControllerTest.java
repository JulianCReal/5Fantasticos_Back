package com.example.fantasticosback.controller;

/*
class RequestControllerTest {
    @Mock
    private RequestService requestService;
    @Mock
    private Group sourceGroup;
    @Mock
    private Group destinationGroup;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Request createRequestDummy() {
        return new Request("1", sourceGroup, destinationGroup, "type", "obs", new java.util.Date(), "E001");
    }

    private RequestDTO createRequestDTODummy() {
        return new RequestDTO("1", "E001", sourceGroup, destinationGroup, "type", "obs", "status", new java.util.Date(), 1, true);
    }

    @Test
    void testCreate() {
        RequestDTO dto = createRequestDTODummy();
        Request request = createRequestDummy();
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.save(request)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        RequestDTO dto = createRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.findAll()).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of requests", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "1";
        Request request = createRequestDummy();
        RequestDTO dto = createRequestDTODummy();
        when(requestService.findById(id)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.getById(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "1";
        // El servicio lanza la excepción si no existe
        when(requestService.findById(id)).thenThrow(new com.example.fantasticosback.Exception.ResourceNotFoundException("Request", "id", id));
        assertThrows(ResourceNotFoundException.class, () -> requestController.getById(id));
    }

    @Test
    void testUpdate_Exists() {
        String id = "1";
        RequestDTO dto = createRequestDTODummy();
        Request request = createRequestDummy();
        when(requestService.findById(id)).thenReturn(request);
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.update(request)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.update(id, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUpdate_NotExists() {
        String id = "1";
        RequestDTO dto = createRequestDTODummy();
        Request request = createRequestDummy();
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.update(request)).thenThrow(new com.example.fantasticosback.Exception.ResourceNotFoundException("Request", "id", id));
        assertThrows(ResourceNotFoundException.class, () -> requestController.update(id, dto));
    }

    @Test
    void testDelete_Exists() {
        String id = "1";
        Request request = createRequestDummy();
        when(requestService.findById(id)).thenReturn(request);
        doNothing().when(requestService).delete(id);
        ResponseEntity<ResponseDTO<Void>> response = requestController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NotExists() {
        String id = "1";
        doThrow(new ResourceNotFoundException("Request", "id", id)).when(requestService).delete(id);
        assertThrows(ResourceNotFoundException.class, () -> requestController.delete(id));
    }

    @Test
    void testGetByStatus() {
        String statusName = "Pending";
        RequestDTO dto = createRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.findByStateName(statusName)).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.getByStatus(statusName);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Requests by status", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}*/
