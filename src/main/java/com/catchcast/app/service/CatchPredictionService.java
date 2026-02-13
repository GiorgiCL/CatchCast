package com.catchcast.app.service;

import com.catchcast.app.dto.CatchRequest;
import com.catchcast.app.dto.CatchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatchPredictionService {

    public CatchResponse predict(CatchRequest request) {

        return new CatchResponse(
                "mvp-rule-v1",
                CatchResponse.PredictionMode.SCORE,
                CatchResponse.ConfidenceLevel.LOW,
                List.of(
                        new CatchResponse.BestHour(
                                request.dateTime().withMinute(0).withSecond(0).withNano(0),
                                0.50,
                                null,
                                List.of("pike", "perch"),
                                List.of("Stub response (no weather yet)", "Next: integrate weather client + scoring")
                        )
                ),
                List.of(
                        new CatchResponse.SpeciesRank("pike", 0.50, null),
                        new CatchResponse.SpeciesRank("perch", 0.45, null)
                )
        );
    }
}
