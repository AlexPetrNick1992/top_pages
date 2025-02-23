package com.example.top.pages.models.converter;

import com.example.top.pages.service.enumerate.StatusEntity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusEntityConverter implements AttributeConverter<StatusEntity, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusEntity statusEntity) {
        return statusEntity != null ? statusEntity.getCode() : null;
    }

    @Override
    public StatusEntity convertToEntityAttribute(Integer dbData) {
        for (StatusEntity status : StatusEntity.values()) {
            if (status.getCode().equals(dbData)) return status;
        }
        throw new IllegalArgumentException("Неизвестный ID: " + dbData);
    }
}
