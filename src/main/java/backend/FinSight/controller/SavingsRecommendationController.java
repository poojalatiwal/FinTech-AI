package backend.FinSight.controller;

import backend.FinSight.dto.SavingsRecommendationResponse;
import backend.FinSight.service.SavingsRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
public class SavingsRecommendationController {

    @Autowired
    private SavingsRecommendationService
            savingsRecommendationService;

    @GetMapping
    public SavingsRecommendationResponse
    getRecommendation(
            Authentication authentication
    ) {

        String userId =
                authentication.getName();

        return savingsRecommendationService
                .getRecommendation(userId);
    }
}