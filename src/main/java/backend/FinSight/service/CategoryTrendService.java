package backend.FinSight.service;

import backend.FinSight.dto.CategoryTrendResponse;

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
public class CategoryTrendService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<CategoryTrendResponse>
    getCategoryTrends(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        Map<String, Double> trendMap =
                new HashMap<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM"
                );

        // GROUP BY MONTH + CATEGORY

        for (Expense expense : expenses) {

            if (expense.getDate() == null) {

                continue;
            }

            String month =
                    expense.getDate()
                            .format(formatter);

            String key =
                    month + "_"
                            + expense.getCategory();

            trendMap.put(
                    key,
                    trendMap.getOrDefault(
                            key,
                            0.0
                    ) + expense.getAmount()
            );
        }

        // RESPONSE

        List<CategoryTrendResponse> response =
                new ArrayList<>();

        for (Map.Entry<String, Double> entry
                : trendMap.entrySet()) {

            String[] parts =
                    entry.getKey().split("_");

            response.add(

                    new CategoryTrendResponse(
                            parts[0],
                            parts[1],
                            entry.getValue()
                    )
            );
        }

        return response;
    }
}

