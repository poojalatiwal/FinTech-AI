package backend.FinSight.dto;

import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class FinancialStabilityResponse {

    private String financialStatus;

    private double debtToExpenseRatio;

    private String message;
}
