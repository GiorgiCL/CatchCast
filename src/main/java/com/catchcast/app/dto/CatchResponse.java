package com.catchcast.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CatchResponse(

        @NotNull
        String modelVersion,

        @NotNull
        PredictionMode mode,

        @NotNull
        ConfidenceLevel confidence,

        @NotNull
        List<BestHour> bestHours,

        @NotNull
        List<SpeciesRank> speciesRanking

) {

    public enum PredictionMode {
        SCORE,
        PROBABILITY
    }

    public enum ConfidenceLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    public record BestHour(
            @NotNull
            LocalDateTime dateTime,

            @NotNull
            @Min(0) @Max(1)
            Double biteScore,

            @Min(0) @Max(100)
            Integer probabilityPercent,

            @NotNull
            List<String> topSpecies,

            @NotNull
            List<String> explanations
    ) {}

    public record SpeciesRank(
            @NotNull
            String species,

            @NotNull
            @Min(0) @Max(1)
            Double avgBiteScore,

            @Min(0) @Max(100)
            Integer avgProbabilityPercent
    ) {}
}
