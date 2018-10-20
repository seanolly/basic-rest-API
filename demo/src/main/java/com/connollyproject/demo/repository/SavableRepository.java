package com.connollyproject.demo.repository;

import com.connollyproject.demo.domain.Savable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Savable DAO.
 */
@Repository
public interface SavableRepository extends CrudRepository<Savable, UUID> {
}