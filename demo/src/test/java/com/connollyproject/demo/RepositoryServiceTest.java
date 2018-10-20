package com.connollyproject.demo;

import com.connollyproject.demo.error.SavableNotFoundException;
import com.connollyproject.demo.repository.SavableRepository;
import com.connollyproject.demo.service.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Transactional
public class RepositoryServiceTest {

    private static final String TEST_FIELD = "test_field";
    private static final int TEST_VALUE = 10;

    private static final String ID_KEY = "uid";

    @Autowired
    private SavableRepository savableRepository;

    private RepositoryService repositoryService;

    @Before
    public void before() {
        this.repositoryService = new RepositoryService(this.savableRepository);
    }

    @Test
    public void create_creates() {
        int size = this.repositoryService.readAll().size();
        Map<String, Object> objectMap = this.repositoryService.create(new HashMap<>());

        assertEquals(size + 1, this.repositoryService.readAll().size());
        assertNotNull(objectMap.get(ID_KEY));
    }

    @Test
    public void delete_deletes() {
        Map<String, Object> object = this.repositoryService.create(new HashMap<>());
        int size = this.repositoryService.readAll().size();
        this.repositoryService.deleteById((UUID) object.get(ID_KEY));
        assertEquals(size - 1, this.repositoryService.readAll().size());
    }

    @Test
    public void delete_does_nothing_when_not_found() {
        Map<String, Object> existing = this.repositoryService.create(new HashMap<>());
        UUID uuid = (UUID) existing.get(ID_KEY);
        this.repositoryService.deleteById(uuid);
        this.repositoryService.deleteById(uuid);
    }

    @Test
    public void update_updates() throws SavableNotFoundException {
        Map<String, Object> existing = this.repositoryService.create(new HashMap<>());
        UUID uuid = (UUID) existing.get(ID_KEY);

        assertNull(existing.get(TEST_FIELD));

        existing.put(TEST_FIELD, TEST_VALUE);
        Map<String, Object> updated = this.repositoryService.updateById(uuid, existing);

        assertEquals(updated.get(TEST_FIELD), TEST_VALUE);
        assertEquals(updated.get(ID_KEY), uuid);
    }

    @Test(expected = SavableNotFoundException.class)
    public void update_throws_if_not_found() throws SavableNotFoundException {
        Map<String, Object> existing = this.repositoryService.create(new HashMap<>());
        UUID uuid = (UUID) existing.get(ID_KEY);
        this.repositoryService.deleteById(uuid);
        this.repositoryService.updateById(uuid, new HashMap<>());
    }

    @Test
    public void read_reads() throws SavableNotFoundException {
        Map<String, Object> existing = this.repositoryService.create(new HashMap<>());
        UUID uuid = (UUID) existing.get(ID_KEY);
        assertEquals(this.repositoryService.read(uuid), existing);
    }

    @Test(expected = SavableNotFoundException.class)
    public void read_throws_if_not_found() throws SavableNotFoundException {
        Map<String, Object> existing = this.repositoryService.create(new HashMap<>());
        UUID uuid = (UUID) existing.get(ID_KEY);
        this.repositoryService.deleteById(uuid);
        this.repositoryService.read(uuid);
    }
}
