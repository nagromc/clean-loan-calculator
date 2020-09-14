package org.example.loancalc.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AmortizationScheduleCalculator {

  private static final BigDecimal NUMBER_OF_MONTHS_IN_A_YEAR = BigDecimal.valueOf(12);
  private static final int PERIOD_OFFSET = 1;
  private final BigDecimal loanAmount;
  private final int durationInMonths;
  private final BigDecimal annualInterestRate;
  private final BigDecimal monthlyInterestRate;
  private BigDecimal monthlyPaymentAmount;
  private final List<Payment> paymentSchedule = new ArrayList<>();

  public AmortizationScheduleCalculator(BigDecimal loanAmount, int durationInMonths, BigDecimal annualInterestRate) {
    this.loanAmount = loanAmount;
    this.durationInMonths = durationInMonths;
    this.annualInterestRate = annualInterestRate;
    this.monthlyInterestRate = annualInterestRate.divide(NUMBER_OF_MONTHS_IN_A_YEAR, 10, RoundingMode.HALF_UP);

    validateInput();
  }

  private void validateInput() {
    if (loanAmount.compareTo(BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException("The loan amount must be positive");
    if (durationInMonths <= 0)
      throw new IllegalArgumentException("The duration must be positive");
    if (annualInterestRate.compareTo(BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException("The interest rate must be positive");
  }

  public AmortizationSchedule execute() {
    this.monthlyPaymentAmount = calculateMonthlyPaymentAmount();

    for (int i = 0; i < this.durationInMonths; i++) {
      Payment payment = calculatePayment(i + PERIOD_OFFSET);
      this.paymentSchedule.add(payment);
    }

    return new AmortizationScheduleBuilder()
      .setAnnualInterestRate(this.annualInterestRate)
      .setDurationInMonths(this.durationInMonths)
      .setLoanAmount(this.loanAmount)
      .setMonthlyPaymentAmount(this.monthlyPaymentAmount)
      .setPayments(this.paymentSchedule)
      .createAmortizationSchedule();
  }

  private BigDecimal calculateMonthlyPaymentAmount() {
    return loanAmount
      .multiply(monthlyInterestRate)
      .divide(
        BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyInterestRate).pow(-durationInMonths, MathContext.DECIMAL32)),
        5,
        RoundingMode.DOWN
      );
  }

  private Payment calculatePayment(int periodNumber) {
    BigDecimal interestAmountOfPeriod = calculateInterestAmountOfPeriod(periodNumber);
    BigDecimal remainingPrincipalAmount = calculateRemainingPrincipalAmountOfPeriod(periodNumber);
    BigDecimal principalAmountOfPeriod = calculatePrincipalAmountOfPeriod(interestAmountOfPeriod);
    BigDecimal totalPaidPrincipalAmount = calculateTotalPaidPrincipalAmount(remainingPrincipalAmount);

    return new PaymentBuilder()
      .setPeriodNumber(periodNumber)
      .setPrincipalAmount(principalAmountOfPeriod)
      .setInterestAmount(interestAmountOfPeriod)
      .setRemainingPrincipalAmount(remainingPrincipalAmount)
      .setTotalPaidPrincipalAmount(totalPaidPrincipalAmount)
      .createPayment();
  }

  private BigDecimal calculateInterestAmountOfPeriod(int periodNumber) {
    if (isFirstPeriod(periodNumber))
      return this.loanAmount.multiply(monthlyInterestRate);

    Payment previousPayment = getPreviousPayment(periodNumber);
    return previousPayment.remainingPrincipalAmount.multiply(monthlyInterestRate);
  }

  private BigDecimal calculateRemainingPrincipalAmountOfPeriod(int periodNumber) {
    return loanAmount.multiply(
      (BigDecimal.ONE.add(monthlyInterestRate)).pow(periodNumber)
    ).subtract(
      monthlyPaymentAmount
        .multiply(
          (BigDecimal.ONE.add(monthlyInterestRate)).pow(periodNumber).subtract(BigDecimal.ONE)
        )
        .divide(monthlyInterestRate, RoundingMode.HALF_UP)
    );
  }

  private BigDecimal calculatePrincipalAmountOfPeriod(BigDecimal interestAmountOfPeriod) {
    return this.monthlyPaymentAmount.subtract(interestAmountOfPeriod);
  }

  private BigDecimal calculateTotalPaidPrincipalAmount(BigDecimal remainingPrincipalAmount) {
    return loanAmount.subtract(remainingPrincipalAmount);
  }

  private boolean isFirstPeriod(int periodNumber) {
    return periodNumber == 1;
  }

  private Payment getPreviousPayment(int periodNumber) {
    return this.paymentSchedule.get(periodNumber - PERIOD_OFFSET - 1);
  }

}
