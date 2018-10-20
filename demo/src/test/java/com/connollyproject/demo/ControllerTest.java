package com.connollyproject.demo;

import com.connollyproject.demo.controller.Controller;
import com.connollyproject.demo.error.APIError;
import com.connollyproject.demo.error.SavableNotFoundException;
import com.connollyproject.demo.service.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.connollyproject.demo.domain.VerbType.GET;
import static com.connollyproject.demo.domain.VerbType.POST;
import static com.connollyproject.demo.domain.VerbType.PUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ControllerTest {

    private static final String INVALID_JSON_ERROR = "Invalid JSON Object";
    private static final String INVALID_UUID_ERROR = "Invalid UUID";

    private static final String VALID_UUID = UUID.randomUUID().toString();
    private static final String INVALID_UUID = "INVALID_UUID";
    private static final String VALID_JSON_STRING = "{\"field\": \"value\"}";
    private static final String INVALID_JSON_STRING = "{\"field\" \"value\"}";

    @Mock
    private RepositoryService repositoryService;

    private Controller controller;

    @Before
    public void before() throws SavableNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.controller = new Controller(repositoryService);

        when(this.repositoryService.create(any())).thenReturn(new HashMap<>());
        when(this.repositoryService.updateById(any(), anyMap())).thenReturn(new HashMap<>());
        when(this.repositoryService.readAll()).thenReturn(new ArrayList<>());
        when(this.repositoryService.read(any())).thenReturn(new HashMap<>());
    }

    @Test
    public void create_creates() {
        ResponseEntity<Object> entity = controller.create(VALID_JSON_STRING);
        Object entity_body = entity.getBody();
        assert (entity_body instanceof Map);
    }

    @Test
    public void create_returns_error_if_invalid_json() {
        ResponseEntity<Object> entity = controller.create(INVALID_JSON_STRING);
        Object entity_body = entity.getBody();

        assert (entity_body instanceof APIError);
        APIError error = (APIError) entity_body;

        assertEquals(POST.toString(), error.getVerb());
        assertEquals("api/objects", error.getUrl());
        assertEquals(INVALID_JSON_ERROR, error.getMessage());
    }

    @Test
    public void update_updates() {
        ResponseEntity<Object> entity = controller.update(VALID_UUID, VALID_JSON_STRING);
        Object entity_body = entity.getBody();
        assert (entity_body instanceof Map);
    }

    @Test
    public void update_returns_error_if_invalid_json() {
        ResponseEntity<Object> entity = controller.update(VALID_UUID, INVALID_JSON_STRING);
        Object entity_body = entity.getBody();

        assert (entity_body instanceof APIError);
        APIError error = (APIError) entity_body;

        assertEquals(PUT.toString(), error.getVerb());
        assertEquals(String.format("api/objects/%s", VALID_UUID), error.getUrl());
        assertEquals(INVALID_JSON_ERROR, error.getMessage());
    }

    @Test
    public void update_returns_error_if_invalid_UUID() {
        ResponseEntity<Object> entity = controller.update(INVALID_UUID, VALID_JSON_STRING);
        Object entity_body = entity.getBody();

        assert (entity_body instanceof APIError);
        APIError error = (APIError) entity_body;

        assertEquals(PUT.toString(), error.getVerb());
        assertEquals(String.format("api/objects/%s", INVALID_UUID), error.getUrl());
        assertEquals(INVALID_UUID_ERROR, error.getMessage());
    }

    @Test
    public void read_reads() {
        ResponseEntity<Object> entity = controller.read(VALID_UUID);
        Object entity_body = entity.getBody();

        assertFalse(entity_body instanceof APIError);
        assert (entity_body instanceof Map);
    }

    @Test
    public void read_returns_error_if_invalid_UUID() {
        ResponseEntity<Object> entity = controller.read(INVALID_UUID);
        Object entity_body = entity.getBody();

        assert (entity_body instanceof APIError);
        APIError error = (APIError) entity_body;
        assertEquals(GET.toString(), error.getVerb());
        assertEquals(String.format("api/objects/%s", INVALID_UUID), error.getUrl());
        assertEquals(INVALID_UUID_ERROR, error.getMessage());
    }
}
