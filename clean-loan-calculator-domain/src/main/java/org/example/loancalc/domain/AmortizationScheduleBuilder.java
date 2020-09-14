package org.example.loancalc.domain;

import java.math.BigDecimal;
import java.util.List;

public class AmortizationScheduleBuilder {

    private BigDecimal loanAmount;
    private int durationInMonths;
    private BigDecimal annualInterestRate;
    private BigDecimal monthlyPaymentAmount;
    private List<Payment> payments;

    public AmortizationScheduleBuilder setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public AmortizationScheduleBuilder setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
        return this;
    }

    public AmortizationScheduleBuilder setAnnualInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
        return this;
    }

    public AmortizationScheduleBuilder setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
        return this;
    }

    public AmortizationScheduleBuilder setPayments(List<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public AmortizationSchedule createAmortizationSchedule() {
        return new AmortizationSchedule(loanAmount, durationInMonths, annualInterestRate, monthlyPaymentAmount, payments);
    }

}
