package backend.FinSight.service;

import backend.FinSight.dto.CategorySummaryResponse;
import backend.FinSight.dto.ExpenseRequest;
import backend.FinSight.dto.ExpenseSummaryResponse;
import backend.FinSight.model.Expense;
import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // ADD EXPENSE

    public Expense addExpense(
            ExpenseRequest request,
            String userId
    ) {

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());

        expense.setAmount(request.getAmount());

        expense.setCategory(request.getCategory());

        expense.setDescription(request.getDescription());

        expense.setDate(request.getDate());

        expense.setUserId(userId);

        return expenseRepository.save(expense);
    }

    // GET USER EXPENSES

    public List<Expense> getExpenses(
            String userId
    ) {

        return expenseRepository.findByUserId(userId);
    }

    // DELETE EXPENSE

    public void deleteExpense(String id) {

        expenseRepository.deleteById(id);
    }

    public ExpenseSummaryResponse getSummary(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        double total = 0;

        double food = 0;

        double travel = 0;

        double shopping = 0;
        double agriculture = 0;
        double other = 0;

        for (Expense expense : expenses) {

            total += expense.getAmount();

            switch (expense.getCategory().toLowerCase()) {

                case "food":
                    food += expense.getAmount();
                    break;

                case "travel":
                    travel += expense.getAmount();
                    break;

                case "shopping":
                    shopping += expense.getAmount();
                    break;

                case "agriculture":
                    agriculture += expense.getAmount();
                    break;

                default:
                    other += expense.getAmount();
                    break;
            }
        }

        return new ExpenseSummaryResponse(
                total,
                food,
                travel,
                shopping,
                agriculture,
                other
        );
    }

    public CategorySummaryResponse getCategorySummary(
            String userId,
            String category
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserIdAndCategory(
                        userId,
                        category
                );

        double total = 0;

        for (Expense expense : expenses) {

            total += expense.getAmount();
        }

        return new CategorySummaryResponse(
                category,
                total,
                expenses.size()
        );
    }
}