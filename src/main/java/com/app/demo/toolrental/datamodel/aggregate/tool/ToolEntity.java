package com.app.demo.toolrental.datamodel.aggregate.tool;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;

@Table("tool")
public class ToolEntity {
    @Id
    private final long id;
    private final String code;
    private final String type;
    private final String brand;
    @MappedCollection(idColumn = "tool_id", keyColumn = "daily_charge_id")
    private final DailyChargeRefEntity dailyChargeRefEntity;

    @PersistenceCreator
    public ToolEntity(long id, String code, String type, String brand, DailyChargeRefEntity dailyChargeRefEntity) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.brand = brand;
        this.dailyChargeRefEntity = dailyChargeRefEntity;
    }

    public static ToolEntityBuilder builder() {
        return new ToolEntityBuilder();
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public DailyChargeRefEntity getDailyChargeRefEntity() {
        return dailyChargeRefEntity;
    }

    public Optional<Long> getDailyChargeId() {
        if (dailyChargeRefEntity == null || dailyChargeRefEntity.getDailyChargeId() == null || dailyChargeRefEntity.getDailyChargeId().getId() == null) {
            return Optional.empty();
        }

        return Optional.of(dailyChargeRefEntity.getDailyChargeId().getId());
    }

    public ToolEntityBuilder toBuilder() {
        return new ToolEntityBuilder().id(this.id).code(this.code).type(this.type).brand(this.brand).dailyChargeRefEntity(this.dailyChargeRefEntity);
    }


    public static final class ToolEntityBuilder {
        private long id;
        private String code;
        private String type;
        private String brand;
        private DailyChargeRefEntity dailyChargeRefEntity;

        private ToolEntityBuilder() {
        }

        public ToolEntityBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ToolEntityBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ToolEntityBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ToolEntityBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public ToolEntityBuilder dailyChargeRefEntity(DailyChargeRefEntity dailyChargeRefEntity) {
            this.dailyChargeRefEntity = dailyChargeRefEntity;
            return this;
        }

        public ToolEntity build() {
            return new ToolEntity(this.id, this.code, this.type, this.brand, this.dailyChargeRefEntity);
        }
    }
}
