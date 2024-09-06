package com.app.demo.toolrental.datamodel.aggregate.dailycharge;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyChargeRepository extends CrudRepository<DailyChargeEntity, Long> {
    Optional<DailyChargeEntity> findByTypeIgnoringCase(String type);
}
