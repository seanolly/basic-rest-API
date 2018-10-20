package com.connollyproject.demo.controller;

import com.connollyproject.demo.domain.VerbType;
import com.connollyproject.demo.error.APIError;
import com.connollyproject.demo.error.InvalidJsonException;
import com.connollyproject.demo.error.InvalidUUIDException;
import com.connollyproject.demo.error.SavableNotFoundException;
import com.connollyproject.demo.service.RepositoryService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.connollyproject.demo.domain.VerbType.*;


@RestController
@RequestMapping("api/objects")
public class Controller {

    private static final Gson jsonParser = new Gson();
    private RepositoryService repositoryService;

    @Autowired
    public Controller(@NonNull RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody String jsonString) {
        try {
            Map<String, Object> jsonObject = parseJSON(jsonString);
            return ok(this.repositoryService.create(jsonObject));
        } catch (InvalidJsonException e) {
            return badRequest(POST, "api/objects", e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> read(@PathVariable(name = "id") @NonNull String id) {
        try {
            UUID uid = parseUUID(id);
            return ok(this.repositoryService.read(uid));
        } catch (SavableNotFoundException | InvalidUUIDException e) {
            return badRequest(GET, String.format("api/objects/%s", id), e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> readAll() {
        return ok(this.repositoryService.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(name = "id") @NonNull String id, @RequestBody String jsonString) {
        try {
            Map<String, Object> jsonObject = parseJSON(jsonString);
            UUID uid = parseUUID(id);
            return ok(this.repositoryService.updateById(uid, jsonObject));
        } catch (InvalidJsonException | SavableNotFoundException | InvalidUUIDException e) {
            return badRequest(PUT, String.format("api/objects/%s", id), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") @NonNull String id) {
        try {
            UUID uid = parseUUID(id);
            this.repositoryService.deleteById(uid);
        } catch (InvalidUUIDException e) {
            // assuming correct behavior is to do nothing
        }
    }

    private ResponseEntity<Object> badRequest(@NonNull VerbType verb, @NonNull String url, @NonNull String msg) {
        return ResponseEntity.badRequest().body(new APIError(verb.toString(), url, msg));
    }

    private ResponseEntity<Object> ok(@NonNull Object o) {
        return ResponseEntity.ok().body(o);
    }

    private Map<String, Object> parseJSON(@NonNull String json) throws InvalidJsonException {
        try {
            return jsonParser.fromJson(json, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            throw new InvalidJsonException();
        }
    }

    private UUID parseUUID(@NonNull String id) throws InvalidUUIDException {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException();
        }
    }
}
