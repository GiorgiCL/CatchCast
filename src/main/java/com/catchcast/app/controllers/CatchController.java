package com.catchcast.app.controllers;

import com.catchcast.app.dto.CatchRequest;
import com.catchcast.app.dto.CatchResponse;
import com.catchcast.app.service.CatchPredictionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CatchController {

    private final CatchPredictionService catchPredictionService;

    public CatchController(CatchPredictionService catchPredictionService) {
        this.catchPredictionService = catchPredictionService;
    }

    @PostMapping("/predict")
    public ResponseEntity<CatchResponse> predict(@Valid @RequestBody CatchRequest request) {
        return ResponseEntity.ok(catchPredictionService.predict(request));
    }
}
