package backend.FinSight.service;

import backend.FinSight.dto.BudgetRequest;
import backend.FinSight.dto.BudgetStatusResponse;
import backend.FinSight.model.Budget;
import backend.FinSight.model.Expense;
import backend.FinSight.repository.BudgetRepository;
import backend.FinSight.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    // CREATE BUDGET
    public Budget createBudget(
            BudgetRequest request,
            String userId
    ) {
        Budget budget = new Budget();
        budget.setCategory(
                request.getCategory()
        );
        budget.setLimitAmount(
                request.getLimitAmount()
        );
        budget.setMonth(
                request.getMonth()
        );
        budget.setUserId(userId);
        return budgetRepository.save(budget);
    }

    // GET USER BUDGETS
    public List<Budget> getBudgets(
            String userId
    ) {
        return budgetRepository.findByUserId(userId);
    }

    // DELETE
    public void deleteBudget(String id) {
        budgetRepository.deleteById(id);
    }

    // UPDATE BUDGET
    public Budget updateBudget(
            String id,
            BudgetRequest request,
            String userId
    ) {
        Budget budget =
                budgetRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Budget not found"
                                )
                        );
        if (!budget.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }
        budget.setCategory(
                request.getCategory()
        );
        budget.setLimitAmount(
                request.getLimitAmount()
        );
        budget.setMonth(
                request.getMonth()
        );
        return budgetRepository.save(budget);
    }

    // BUDGET STATUS
    public BudgetStatusResponse getBudgetStatus(
            String userId,
            String category,
            String month
    ) {
        Budget budget =
                budgetRepository
                        .findByUserIdAndCategoryAndMonth(
                                userId,
                                category,
                                month
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Budget not found"
                                )
                        );

        List<Expense> expenses =
                expenseRepository
                        .findByUserIdAndCategory(
                                userId,
                                category
                        );

        double spent = 0;

        for (Expense expense : expenses) {
            spent += expense.getAmount();
        }

        double remaining =
                budget.getLimitAmount() - spent;
        String status;

        if (remaining < 0) {
            status = "OVER_LIMIT";
        } else if (remaining < 1000) {
            status = "WARNING";
        } else {
            status = "SAFE";
        }
        return new BudgetStatusResponse(
                category,
                budget.getLimitAmount(),
                spent,
                remaining,
                status
        );
    }
}