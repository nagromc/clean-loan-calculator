package org.example.domain;

import java.math.BigDecimal;

public class PaymentBuilder {

    private int periodNumber;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal remainingPrincipalAmount;
    private BigDecimal totalPaidPrincipalAmount;

    public PaymentBuilder setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
        return this;
    }

    public PaymentBuilder setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
        return this;
    }

    public PaymentBuilder setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
        return this;
    }

    public PaymentBuilder setRemainingPrincipalAmount(BigDecimal remainingPrincipalAmount) {
        this.remainingPrincipalAmount = remainingPrincipalAmount;
        return this;
    }

    public PaymentBuilder setTotalPaidPrincipalAmount(BigDecimal totalPaidPrincipalAmount) {
        this.totalPaidPrincipalAmount = totalPaidPrincipalAmount;
        return this;
    }

    public Payment createPayment() {
        return new Payment(periodNumber, principalAmount, interestAmount, remainingPrincipalAmount, totalPaidPrincipalAmount);
    }

}
