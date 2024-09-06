package com.app.demo.toolrental.transport;

import com.app.demo.toolrental.datamodel.aggregate.dailycharge.DailyChargeEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class DailyChargeDTO {
    private final Long id;
    private final BigDecimal price;
    private final Boolean chargeOnWeekdays;
    private final Boolean chargeOnWeekend;
    private final Boolean chargeOnHoliday;

    public DailyChargeDTO(Long id, BigDecimal price, Boolean chargeOnWeekdays, Boolean chargeOnWeekend, Boolean chargeOnHoliday) {
        this.id = id;
        this.price = price;
        this.chargeOnWeekdays = chargeOnWeekdays;
        this.chargeOnWeekend = chargeOnWeekend;
        this.chargeOnHoliday = chargeOnHoliday;
    }

    public static DailyChargeDTOBuilder builder() {
        return new DailyChargeDTOBuilder();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getChargeOnWeekdays() {
        return chargeOnWeekdays;
    }

    public Boolean getChargeOnWeekend() {
        return chargeOnWeekend;
    }

    public Boolean getChargeOnHoliday() {
        return chargeOnHoliday;
    }

    public DailyChargeDTOBuilder toBuilder() {
        return new DailyChargeDTOBuilder().id(this.id).price(this.price).chargeOnHoliday(this.chargeOnHoliday)
                .chargeOnWeekend(this.chargeOnWeekend).chargeOnWeekdays(this.chargeOnWeekdays);
    }

    public static DailyChargeDTO convertToDTO(DailyChargeEntity entity) {
        return DailyChargeDTO.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .chargeOnWeekdays(entity.isChargeOnWeekdays())
                .chargeOnWeekend(entity.isChargeOnWeekend())
                .chargeOnHoliday(entity.isChargeOnHoliday())
                .build();
    }

    public static DailyChargeEntity convertToEntity(DailyChargeDTO dto) {
        return DailyChargeEntity.builder()
                .id(Optional.ofNullable(dto.getId()).orElse(0L))
                .price(dto.price)
                .chargeOnWeekdays(dto.chargeOnWeekdays)
                .chargeOnWeekend(dto.chargeOnWeekend)
                .chargeOnHoliday(dto.chargeOnHoliday).build();
    }

    public static final class DailyChargeDTOBuilder {
        private Long id;
        private BigDecimal price;
        private Boolean chargeOnWeekdays;
        private Boolean chargeOnWeekend;
        private Boolean chargeOnHoliday;

        private DailyChargeDTOBuilder() {
        }

        public DailyChargeDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DailyChargeDTOBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public DailyChargeDTOBuilder chargeOnWeekdays(Boolean chargeOnWeekdays) {
            this.chargeOnWeekdays = chargeOnWeekdays;
            return this;
        }

        public DailyChargeDTOBuilder chargeOnWeekend(Boolean chargeOnWeekend) {
            this.chargeOnWeekend = chargeOnWeekend;
            return this;
        }

        public DailyChargeDTOBuilder chargeOnHoliday(Boolean chargeOnHoliday) {
            this.chargeOnHoliday = chargeOnHoliday;
            return this;
        }

        public DailyChargeDTO build() {
            return new DailyChargeDTO(id, price, chargeOnWeekdays, chargeOnWeekend, chargeOnHoliday);
        }
    }
}
