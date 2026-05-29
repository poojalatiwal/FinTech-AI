package backend.FinSight.controller;

import backend.FinSight.dto.CategorySummaryResponse;
import backend.FinSight.dto.ExpenseRequest;
import backend.FinSight.dto.ExpenseSummaryResponse;
import backend.FinSight.model.Expense;
import backend.FinSight.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // ADD EXPENSE

    @PostMapping
    public Expense addExpense(
            @RequestBody ExpenseRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return expenseService.addExpense(
                request,
                userId
        );
    }

    // GET USER EXPENSES

    @GetMapping
    public List<Expense> getExpenses(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return expenseService.getExpenses(userId);
    }

    // DELETE EXPENSE

    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable String id
    ) {

        expenseService.deleteExpense(id);

        return "Expense deleted successfully";
    }

    // SUMMARY API

    @GetMapping("/summary")
    public ExpenseSummaryResponse getSummary(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return expenseService.getSummary(userId);
    }

    @GetMapping("/category/{category}")
    public CategorySummaryResponse getCategorySummary(
            @PathVariable String category,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return expenseService.getCategorySummary(
                userId,
                category
        );
    }
}