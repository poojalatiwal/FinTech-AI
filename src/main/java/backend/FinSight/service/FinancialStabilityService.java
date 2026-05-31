package backend.FinSight.service;

import backend.FinSight.dto.FinancialStabilityResponse;

import backend.FinSight.model.Expense;

import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialStabilityService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public FinancialStabilityResponse
    analyzeFinancialHealth(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        // NO DATA

        if (expenses.isEmpty()) {

            return new FinancialStabilityResponse(
                    "Unknown",
                    0,
                    "No financial data available."
            );
        }

        // TOTALS

        double totalExpenses = 0;

        double debtExpenses = 0;

        for (Expense expense : expenses) {

            // TOTAL EXPENSES

            totalExpenses +=
                    expense.getAmount();

            // DEBT RELATED EXPENSES

            if (expense.isDebtRelated()) {

                debtExpenses +=
                        expense.getAmount();
            }
        }

        // SAFETY CHECK

        if (totalExpenses == 0) {

            return new FinancialStabilityResponse(
                    "Unknown",
                    0,
                    "Unable to analyze financial data."
            );
        }

        // DEBT RATIO

        double ratio =
                (debtExpenses / totalExpenses)
                        * 100;

        // ROUND OFF

        ratio =
                Math.round(ratio * 100.0)
                        / 100.0;

        String status;

        String message;

        // AI ANALYSIS

        if (ratio < 30) {

            status = "Stable";

            message =
                    "Your financial condition looks healthy.";

        } else if (ratio < 60) {

            status = "Moderate";

            message =
                    "Your debt expenses are increasing. Manage spending carefully.";

        } else {

            status = "Risky";

            message =
                    "High debt burden detected. Reduce liabilities and unnecessary expenses.";
        }

        return new FinancialStabilityResponse(
                status,
                ratio,
                message
        );
    }
}
