package com.app.demo.toolrental.datamodel.aggregate.dailycharge;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("daily_charge")
public class DailyChargeEntity {
    @Id
    private final long id;
    private final String type;
    private final BigDecimal price;
    private final boolean chargeOnWeekdays;
    private final boolean chargeOnWeekend;
    private final boolean chargeOnHoliday;

    @PersistenceCreator
    public DailyChargeEntity(long id, String type, BigDecimal price, boolean chargeOnWeekdays, boolean chargeOnWeekend, boolean chargeOnHoliday) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.chargeOnWeekdays = chargeOnWeekdays;
        this.chargeOnWeekend = chargeOnWeekend;
        this.chargeOnHoliday = chargeOnHoliday;
    }

    public static DailyChargeEntityBuilder builder() {
        return new DailyChargeEntityBuilder();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isChargeOnWeekdays() {
        return chargeOnWeekdays;
    }

    public boolean isChargeOnWeekend() {
        return chargeOnWeekend;
    }

    public boolean isChargeOnHoliday() {
        return chargeOnHoliday;
    }

    public String getType() {
        return type;
    }

    public DailyChargeEntityBuilder toBuilder() {
        return new DailyChargeEntityBuilder().id(this.id).type(this.type).price(this.price).chargeOnWeekend(this.chargeOnWeekend)
                .chargeOnWeekend(this.chargeOnWeekend).chargeOnHoliday(this.chargeOnHoliday);
    }


    public static final class DailyChargeEntityBuilder {
        private long id;
        private String type;
        private BigDecimal price;
        private boolean chargeOnWeekdays;
        private boolean chargeOnWeekend;
        private boolean chargeOnHoliday;

        private DailyChargeEntityBuilder() {
        }

        public DailyChargeEntityBuilder id(long id) {
            this.id = id;
            return this;
        }

        public DailyChargeEntityBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DailyChargeEntityBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public DailyChargeEntityBuilder chargeOnWeekdays(boolean chargeOnWeekdays) {
            this.chargeOnWeekdays = chargeOnWeekdays;
            return this;
        }

        public DailyChargeEntityBuilder chargeOnWeekend(boolean chargeOnWeekend) {
            this.chargeOnWeekend = chargeOnWeekend;
            return this;
        }

        public DailyChargeEntityBuilder chargeOnHoliday(boolean chargeOnHoliday) {
            this.chargeOnHoliday = chargeOnHoliday;
            return this;
        }

        public DailyChargeEntity build() {
            return new DailyChargeEntity(id, type, price, chargeOnWeekdays, chargeOnWeekend, chargeOnHoliday);
        }
    }
}
