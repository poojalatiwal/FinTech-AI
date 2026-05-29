package backend.FinSight.service;

import backend.FinSight.dto.SavingsRecommendationResponse;

import backend.FinSight.model.Expense;

import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SavingsRecommendationService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public SavingsRecommendationResponse getRecommendation(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        if (expenses.isEmpty()) {

            return new SavingsRecommendationResponse(
                    "NONE",
                    "No expense data available.",
                    0
            );
        }

        // CATEGORY TOTALS

        Map<String, Double> categoryTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            String category =
                    expense.getCategory();

            categoryTotals.put(
                    category,
                    categoryTotals.getOrDefault(
                            category,
                            0.0
                    ) + expense.getAmount()
            );
        }

        // HIGHEST CATEGORY

        String highestCategory = "";

        double highestAmount = 0;

        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            if (entry.getValue() > highestAmount) {

                highestAmount =
                        entry.getValue();

                highestCategory =
                        entry.getKey();
            }
        }

        // ESTIMATED SAVINGS

        double estimatedSavings =
                highestAmount * 0.15;

        String suggestion =
                "Reduce "
                        + highestCategory
                        + " spending by 15% to save ₹"
                        + estimatedSavings
                        + " monthly.";

        return new SavingsRecommendationResponse(
                highestCategory,
                suggestion,
                estimatedSavings
        );
    }
}