package backend.FinSight.controller;

import backend.FinSight.dto.PredictionResponse;
import backend.FinSight.service.MlPredictionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ml")
public class MlPredictionController {

    @Autowired
    private MlPredictionService mlPredictionService;

    @GetMapping("/predict")
    public PredictionResponse predictExpense(
            Authentication authentication
    ) {
        String userId =
                authentication.getName();
        return mlPredictionService
                .predictNextMonthExpense(userId);
    }
}