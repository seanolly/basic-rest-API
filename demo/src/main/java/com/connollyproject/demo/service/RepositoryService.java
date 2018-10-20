package com.connollyproject.demo.service;

import com.connollyproject.demo.domain.Savable;
import com.connollyproject.demo.error.SavableNotFoundException;
import com.connollyproject.demo.repository.SavableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RepositoryService {

    private final SavableRepository savableRepository;

    @Autowired
    public RepositoryService(@NonNull SavableRepository savableRepository) {
        this.savableRepository = savableRepository;
    }

    public Map<String, Object> create(@NonNull Map<String, Object> json) {
        Savable savable = this.savableRepository.save(new Savable(json));
        return Savable.convertToMap(savable);
    }

    public Map<String, Object> read(@NonNull UUID uid) throws SavableNotFoundException {
        return Savable.convertToMap(findOrFail(uid));
    }

    public List<String> readAll() {
        List<String> results = new ArrayList<>();
        this.savableRepository.findAll().forEach(savable -> results.add("api/objects/" + savable.getId().toString()));
        return results;
    }

    public Map<String, Object> updateById(@NonNull UUID uid, @NonNull Map<String, Object> update) throws SavableNotFoundException {
        Savable existing = findOrFail(uid);
        existing.setContent(update);
        return Savable.convertToMap(this.savableRepository.save(existing));
    }

    public void deleteById(@NonNull UUID uid) {
        if (this.savableRepository.findById(uid).isPresent()) {
            this.savableRepository.deleteById(uid);
        }
    }

    private Savable findOrFail(@NonNull UUID uid) throws SavableNotFoundException {
        return this.savableRepository.findById(uid).orElseThrow(() -> new SavableNotFoundException(uid));
    }
}