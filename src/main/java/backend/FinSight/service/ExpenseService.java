package backend.FinSight.service;

import backend.FinSight.dto.CategorySummaryResponse;
import backend.FinSight.dto.ExpenseRequest;
import backend.FinSight.dto.ExpenseSummaryResponse;
import backend.FinSight.model.Expense;
import backend.FinSight.model.User;
import backend.FinSight.repository.ExpenseRepository;

import backend.FinSight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired private NotificationService notificationService;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepository;

    // ADD EXPENSE

    public Expense addExpense(
            ExpenseRequest request,
            String userId
    ) {

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());

        expense.setAmount(request.getAmount());

        expense.setCategory(request.getCategory());

        expense.setDebtRelated(
                request.isDebtRelated()
        );

        expense.setDescription(
                request.getDescription()
        );

        expense.setDate(request.getDate());

        expense.setUserId(userId);

        Expense savedExpense =
                expenseRepository.save(expense);

        // GET USER EMAIL
        User user =
                userRepository.findByUsername(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        String email =
                user.getEmail();
        // HIGH EXPENSE ALERT

        if (expense.getAmount() > 10000) {

            String message =
                    "High expense detected: ₹"
                            + expense.getAmount();

            // APP NOTIFICATION

            notificationService.createNotification(
                    userId,
                    message
            );

            // EMAIL ALERT

            emailService.sendEmail(
                    email,
                    "FinSight High Expense Alert",
                    message
            );
        }

        // FOOD SPENDING ALERT
        if (expense.getCategory()
                .equalsIgnoreCase("Food")
                && expense.getAmount() > 3000) {

            String message =
                    "Food spending exceeded healthy limit.";

            notificationService.createNotification(
                    userId,
                    message
            );

            emailService.sendEmail(
                    email,
                    "FinSight Food Alert",
                    message
            );
        }

        return savedExpense;
    }


    // GET USER EXPENSES

    public List<Expense> getExpenses(
            String userId
    ) {

        return expenseRepository.findByUserId(userId);
    }

    // UPDATE EXPENSE

    public Expense updateExpense(
            String id,
            ExpenseRequest request,
            String userId
    ) {

        Expense expense =
                expenseRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found"
                                )
                        );

        if (!expense.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "Unauthorized access"
            );
        }

        expense.setTitle(request.getTitle());

        expense.setAmount(request.getAmount());

        expense.setCategory(request.getCategory());

        expense.setDebtRelated( request.isDebtRelated() );

        expense.setDescription(request.getDescription());

        expense.setDate(request.getDate());

        return expenseRepository.save(expense);
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

    // FILTER BY CATEGORY

    public List<Expense> getExpensesByCategory(
            String userId,
            String category
    ) {

        return expenseRepository
                .findByUserIdAndCategory(
                        userId,
                        category
                );
    }

    // Monthly Expense
    public List<Expense> getMonthlyExpenses(
            String userId
    ) {

        LocalDate now = LocalDate.now();

        LocalDate start =
                now.withDayOfMonth(1);

        LocalDate end =
                now.withDayOfMonth(
                        now.lengthOfMonth()
                );

        return expenseRepository
                .findByUserIdAndDateBetween(
                        userId,
                        start,
                        end
                );
    }

    // TOTAL SPENDING

    public double getTotalExpenses(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // RECENT EXPENSES

    public List<Expense> getRecentExpenses(
            String userId
    ) {

        return expenseRepository
                .findTop5ByUserIdOrderByDateDesc(
                        userId
                );
    }
}