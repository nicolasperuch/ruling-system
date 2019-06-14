package com.github.nicolasperuch.repository;

import com.github.nicolasperuch.entity.RulingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulingRepository extends CrudRepository<RulingEntity, Integer> {
}
