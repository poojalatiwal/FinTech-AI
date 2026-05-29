package backend.FinSight.service;

import backend.FinSight.dto.PredictionResponse;
import backend.FinSight.model.Expense;
import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MlPredictionService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public PredictionResponse predictNextMonthExpense(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository
                        .findByUserIdOrderByDateAsc(userId);

        if (expenses.isEmpty()) {

            return new PredictionResponse(
                    0,
                    "No expense data available"
            );
        }

        // MONTHLY TOTALS

        Map<YearMonth, Double> monthlyTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            if (expense.getDate() == null) {
                continue;
            }

            YearMonth month =
                    YearMonth.from(expense.getDate());

            monthlyTotals.put(
                    month,
                    monthlyTotals.getOrDefault(
                            month,
                            0.0
                    ) + expense.getAmount()
            );
        }

        // AVERAGE

        double total = 0;

        for (double value : monthlyTotals.values()) {

            total += value;
        }

        double predicted =
                total / monthlyTotals.size();

        String message;

        if (predicted > 20000) {

            message =
                    "Your expenses may increase significantly next month.";

        } else if (predicted > 10000) {

            message =
                    "You are maintaining moderate spending.";

        } else {

            message =
                    "Your spending pattern looks controlled.";
        }

        return new PredictionResponse(
                predicted,
                message
        );
    }
}