package backend.FinSight.service;

import backend.FinSight.dto.FraudResponse;

import backend.FinSight.model.Expense;

import backend.FinSight.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudDetectionService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public FraudResponse detectFraud(
            String userId
    ) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        // CHECK FOR HIGH VALUE

        for (Expense expense : expenses) {

            // VERY HIGH AMOUNT

            if (expense.getAmount() > 50000) {

                return new FraudResponse(
                        true,
                        "Suspicious high-value transaction detected."
                );
            }

            // UNUSUAL CATEGORY

            if (expense.getCategory()
                    .equalsIgnoreCase("gambling")) {

                return new FraudResponse(
                        true,
                        "Suspicious category detected."
                );
            }
        }

        return new FraudResponse(
                false,
                "No suspicious activity detected."
        );
    }
}

