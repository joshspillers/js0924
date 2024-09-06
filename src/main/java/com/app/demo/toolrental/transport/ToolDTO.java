package com.app.demo.toolrental.transport;

import com.app.demo.toolrental.datamodel.aggregate.tool.DailyChargeRefEntity;
import com.app.demo.toolrental.datamodel.aggregate.tool.ToolEntity;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.util.Optional;

public class ToolDTO {
    private final Long id;
    private final String code;
    private final String type;
    private final String brand;
    private final Long dailyChargeId;

    public ToolDTO(Long id, String code, String type, String brand, Long dailyChargeId) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.brand = brand;
        this.dailyChargeId = dailyChargeId;
    }

    public static ToolDTOBuilder builder() {
        return new ToolDTOBuilder();
    }

    public Long getId() {
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

    public Long getDailyChargeId() {
        return dailyChargeId;
    }

    public ToolDTOBuilder dtoBuilder() {
        return new ToolDTOBuilder().id(this.id).code(this.code).type(this.type).brand(this.brand).dailyChargeId(this.dailyChargeId);
    }

    public static ToolDTO convertToDTO(ToolEntity entity) {
        return ToolDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .type(entity.getType())
                .brand(entity.getBrand())
                .dailyChargeId(entity.getDailyChargeId().orElse(null))
                .build();
    }

    public static ToolEntity convertToEntity(ToolDTO dto) {
        ToolEntity.ToolEntityBuilder builder = ToolEntity.builder()
                .id(Optional.ofNullable(dto.getId()).orElse(0L))
                .code(dto.getCode())
                .type(dto.getType())
                .brand(dto.getBrand());
        Optional.ofNullable(dto.getDailyChargeId()).ifPresentOrElse(
                v-> builder.dailyChargeRefEntity(DailyChargeRefEntity.builder().dailyChargeId(AggregateReference.to(v)).build()),
                ()-> builder.dailyChargeRefEntity(null)
        );
        return builder.build();
    }

    public static final class ToolDTOBuilder {
        private Long id;
        private String code;
        private String type;
        private String brand;
        private Long dailyChargeId;

        private ToolDTOBuilder() {
        }

        public ToolDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ToolDTOBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ToolDTOBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ToolDTOBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public ToolDTOBuilder dailyChargeId(Long dailyChargeId) {
            this.dailyChargeId = dailyChargeId;
            return this;
        }

        public ToolDTO build() {
            return new ToolDTO(id, code, type, brand, dailyChargeId);
        }
    }
}
