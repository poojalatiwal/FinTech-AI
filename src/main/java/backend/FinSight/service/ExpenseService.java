package backend.FinSight.service;

import backend.FinSight.dto.ExpenseRequest;
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
}