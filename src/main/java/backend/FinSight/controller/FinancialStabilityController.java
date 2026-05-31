package backend.FinSight.controller;

import backend.FinSight.dto.FinancialStabilityResponse;

import backend.FinSight.service.FinancialStabilityService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial-health")
public class FinancialStabilityController {

    @Autowired
    private FinancialStabilityService
            financialStabilityService;

    @GetMapping
    public FinancialStabilityResponse
    analyzeFinancialHealth(
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return financialStabilityService
                .analyzeFinancialHealth(userId);
    }
}
