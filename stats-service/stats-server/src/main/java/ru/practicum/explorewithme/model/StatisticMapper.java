package ru.practicum.explorewithme.model;

import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;

/**
 * Mapper class for converting between StatisticEntity and DTOs.
 */
public class StatisticMapper {
    protected StatisticMapper() {
    }

    /**
     * Converts a StatisticRequest to a StatisticEntity.
     *
     * @param request the StatisticRequest to convert
     * @return the converted StatisticEntity
     */
    public static StatisticEntity toEntity(final StatisticRequest request) {
        return StatisticEntity.builder()
                .app(request.getApp())
                .uri(request.getUri())
                .ip(request.getIp())
                .creationTime(request.getCreationTime())
                .build();
    }

    /**
     * Converts a StatisticEntity to a StatisticResponse.
     *
     * @param entity the StatisticEntity to convert
     * @return the converted StatisticResponse
     */
    public static StatisticResponse toResponse(final StatisticEntity entity) {
        return StatisticResponse.builder()
                .app(entity.getApp())
                .uri(entity.getUri())
                .ip(entity.getIp())
                .creationTime(entity.getCreationTime())
                .id(entity.getId())
                .build();
    }
}
