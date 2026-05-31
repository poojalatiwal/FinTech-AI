package backend.FinSight.service;

import backend.FinSight.dto.ForecastResponse;

import backend.FinSight.model.Expense;

import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public ForecastResponse forecastExpenses(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        if (expenses.isEmpty()) {

            return new ForecastResponse(
                    0,
                    "No expense data available."
            );
        }

        // TOTAL

        double total = 0;

        for (Expense expense : expenses) {

            total += expense.getAmount();
        }

        // SIMPLE AI/ML STYLE FORECAST

        double average =
                total / expenses.size();

        double predicted =
                average * 1.20;

        String message;

        if (predicted > average) {

            message =
                    "Your expenses may increase next month.";

        } else {

            message =
                    "Your spending looks stable.";
        }

        return new ForecastResponse(
                predicted,
                message
        );
    }
}

