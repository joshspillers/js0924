package com.app.demo.toolrental;

import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeEntity;
import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeRepository;
import com.app.demo.toolrental.datamodel.aggregate.tool.DailyChargeRefEntity;
import com.app.demo.toolrental.datamodel.aggregate.tool.ToolEntity;
import com.app.demo.toolrental.datamodel.aggregate.tool.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class DatabaseLoader implements CommandLineRunner {
    private final ToolRepository toolRepository;
    private final DailyChargeRepository dailyChargeRepository;

    @Autowired
    public DatabaseLoader(ToolRepository toolRepository, DailyChargeRepository dailyChargeRepository) {
        this.toolRepository = toolRepository;
        this.dailyChargeRepository = dailyChargeRepository;
    }

    @Override
    //This loads our in-memory database (H2 in this case) with Tool info and Daily Charge info.
    public void run(String... args) throws Exception {
        System.out.println("Loading database...");
        List<DailyChargeEntity> dailyChargeEntityList = new ArrayList<>();
        dailyChargeEntityList.add(DailyChargeEntity.builder().type("Ladder").price(new BigDecimal("1.99"))
                .chargeOnWeekdays(true).chargeOnWeekend(true).chargeOnHoliday(false).build());
        dailyChargeEntityList.add(DailyChargeEntity.builder().type("Chainsaw").price(new BigDecimal("1.49"))
                .chargeOnWeekdays(true).chargeOnWeekend(false).chargeOnHoliday(true).build());
        dailyChargeEntityList.add(DailyChargeEntity.builder().type("Jackhammer").price(new BigDecimal("2.99"))
                .chargeOnWeekdays(true).chargeOnWeekend(false).chargeOnHoliday(false).build());

        dailyChargeRepository.saveAll(dailyChargeEntityList);

        long chainsawTypeChargeId = dailyChargeRepository.findByTypeIgnoringCase("Chainsaw").orElseThrow().getId();
        long ladderTypeChargeId = dailyChargeRepository.findByTypeIgnoringCase("Ladder").orElseThrow().getId();
        long jackhammerTypeChargeId = dailyChargeRepository.findByTypeIgnoringCase("jackhammer").orElseThrow().getId();

        List<ToolEntity> toolList = new ArrayList<>();
        toolList.add(ToolEntity.builder().code("CHNS").type("Chainsaw").brand("Stihl")
                .dailyChargeRefEntity(DailyChargeRefEntity.builder().dailyChargeId(AggregateReference.to(chainsawTypeChargeId)).build())
                .build());
        toolList.add(ToolEntity.builder().code("LADW").type("Ladder").brand("Werner")
                .dailyChargeRefEntity(DailyChargeRefEntity.builder().dailyChargeId(AggregateReference.to(ladderTypeChargeId)).build())
                .build());
        toolList.add(ToolEntity.builder().code("JAKD").type("Jackhammer").brand("DeWalt")
                .dailyChargeRefEntity(DailyChargeRefEntity.builder().dailyChargeId(AggregateReference.to(jackhammerTypeChargeId)).build())
                .build());
        toolList.add(ToolEntity.builder().code("JAKR").type("Jackhammer").brand("Ridgid")
                .dailyChargeRefEntity(DailyChargeRefEntity.builder().dailyChargeId(AggregateReference.to(jackhammerTypeChargeId)).build())
                .build());

        toolRepository.saveAll(toolList);
        System.out.println("Finished loading database");
    }
}
