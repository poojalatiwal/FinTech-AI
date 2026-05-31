package backend.FinSight.service;

import backend.FinSight.dto.MonthlyTrendResponse;

import backend.FinSight.model.Expense;

import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrendService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<MonthlyTrendResponse>
    getMonthlyTrend(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        Map<String, Double> monthlyTotals =
                new HashMap<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM"
                );

        // MONTHLY TOTALS

        for (Expense expense : expenses) {

            if (expense.getDate() == null) {

                continue;
            }

            String month =
                    expense.getDate()
                            .format(formatter);

            monthlyTotals.put(
                    month,
                    monthlyTotals.getOrDefault(
                            month,
                            0.0
                    ) + expense.getAmount()
            );
        }

        // RESPONSE

        List<MonthlyTrendResponse> response =
                new ArrayList<>();

        for (Map.Entry<String, Double> entry
                : monthlyTotals.entrySet()) {

            response.add(

                    new MonthlyTrendResponse(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        return response;
    }
}
