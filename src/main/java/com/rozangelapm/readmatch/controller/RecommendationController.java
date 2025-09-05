package com.rozangelapm.readmatch.controller;

import com.rozangelapm.readmatch.dto.PreferenceResquest;
import com.rozangelapm.readmatch.dto.RecommendationResponse;
import com.rozangelapm.readmatch.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {

    private final PreferenceService preferenceService;

    public RecommendationController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping("/api/v1/preferences")
    public ResponseEntity<Void> postPreference(@Valid @RequestBody PreferenceResquest preferenceDto){

        preferenceService.savePreferences(preferenceDto);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/api/v1/recommendations")
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(){

        List<RecommendationResponse> recommendationList = preferenceService.findRecommendations();

        return ResponseEntity.ok(recommendationList);

    }

}
