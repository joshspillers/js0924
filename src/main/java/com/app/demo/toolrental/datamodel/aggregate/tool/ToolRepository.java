package com.app.demo.toolrental.datamodel.aggregate.tool;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRepository extends CrudRepository<ToolEntity,Long> {
    Optional<ToolEntity> findByCodeIgnoreCase(String code);
}
