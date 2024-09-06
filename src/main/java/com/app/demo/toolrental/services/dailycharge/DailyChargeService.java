package com.app.demo.toolrental.services.dailycharge;

import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeEntity;
import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeRepository;
import com.app.demo.toolrental.transport.DailyChargeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DailyChargeService {
    private final DailyChargeRepository dailyChargeRepository;

    @Autowired
    public DailyChargeService(DailyChargeRepository dailyChargeRepository) {
        this.dailyChargeRepository = dailyChargeRepository;
    }

    public Optional<DailyChargeDTO> getDailyChargeById(long id) {
        if (id==0) {
            return Optional.empty();
        }

        Optional<DailyChargeEntity> dailyChargeEntity = dailyChargeRepository.findById(id);
        return dailyChargeEntity.map(DailyChargeDTO::convertToDTO);
    }
}
