package com.app.demo.toolrental.datamodel.aggregate.tool;

import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeEntity;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

@Table("tool_daily_charge_ref")
public class DailyChargeRefEntity {
    private final AggregateReference<DailyChargeEntity, Long> dailyChargeId;

    public DailyChargeRefEntity(AggregateReference<DailyChargeEntity, Long> dailyChargeId) {
        this.dailyChargeId = dailyChargeId;
    }

    public static DailyChargeRefEntityBuilder builder() {
        return new DailyChargeRefEntityBuilder();
    }

    public AggregateReference<DailyChargeEntity, Long> getDailyChargeId() {
        return dailyChargeId;
    }

    public static final class DailyChargeRefEntityBuilder {
        private AggregateReference<DailyChargeEntity, Long> dailyChargeId;

        private DailyChargeRefEntityBuilder() {
        }

        public DailyChargeRefEntityBuilder dailyChargeId(AggregateReference<DailyChargeEntity, Long> dailyChargeId) {
            this.dailyChargeId = dailyChargeId;
            return this;
        }

        public DailyChargeRefEntity build() {
            return new DailyChargeRefEntity(dailyChargeId);
        }
    }
}
