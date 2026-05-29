package backend.FinSight.service;

import backend.FinSight.dto.FinancialHealthResponse;

import backend.FinSight.model.Budget;
import backend.FinSight.model.Expense;

import backend.FinSight.repository.BudgetRepository;
import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialHealthService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    public FinancialHealthResponse calculateHealthScore(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        if (expenses.isEmpty()) {

            return new FinancialHealthResponse(
                    0,
                    "NO_DATA",
                    "No expenses available."
            );
        }

        double total = 0;

        for (Expense expense : expenses) {

            total += expense.getAmount();
        }

        double average =
                total / expenses.size();

        int score = 100;

        // HIGH AVERAGE SPENDING

        if (average > 20000) {

            score -= 40;

        } else if (average > 10000) {

            score -= 20;
        }

        // LARGE EXPENSE DETECTION

        for (Expense expense : expenses) {

            if (expense.getAmount() > average * 2) {

                score -= 10;
            }
        }

        // BUDGET CHECK

        List<Budget> budgets =
                budgetRepository.findByUserId(userId);

        for (Budget budget : budgets) {

            double categoryExpense = 0;

            for (Expense expense : expenses) {

                if (expense.getCategory()
                        .equalsIgnoreCase(
                                budget.getCategory()
                        )) {

                    categoryExpense +=
                            expense.getAmount();
                }
            }

            // BUDGET EXCEEDED

            if (categoryExpense >
                    budget.getLimitAmount()) {

                score -= 25;
            }
        }

        // MIN SCORE

        if (score < 0) {

            score = 0;
        }

        String status;

        String advice;

        if (score >= 80) {

            status = "EXCELLENT";

            advice =
                    "Your financial spending habits are excellent.";

        } else if (score >= 60) {

            status = "GOOD";

            advice =
                    "Your spending is mostly under control.";

        } else if (score >= 40) {

            status = "WARNING";

            advice =
                    "You should reduce unnecessary expenses.";

        } else {

            status = "CRITICAL";

            advice =
                    "Your spending pattern is risky.";
        }

        return new FinancialHealthResponse(
                score,
                status,
                advice
        );
    }
}