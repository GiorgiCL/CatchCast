package com.catchcast.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CatchRequest(
        @NotNull
        LocalDateTime dateTime,

        @NotNull
        @Min(-90) @Max(90)
        Double latitude,

        @NotNull
        @Min(-180) @Max(180)
        Double longitude,

        @NotNull
        WaterType waterType,

        @NotNull
        @Min(1) @Max(72)
        Integer horizonHours,

        String targetSpecies,
        String technique,

        @Min(1) @Max(5)
        Integer skillLevel,

        Long gearSetId
) {
    public enum WaterType {
        LAKE,
        RIVER,
        RESERVOIR
    }
}
