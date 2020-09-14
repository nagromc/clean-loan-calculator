package org.example.loancalc.domain;

import java.math.BigDecimal;
import java.util.List;

public class AmortizationSchedule {

  public final BigDecimal loanAmount;
  public final int durationInMonths;
  public final BigDecimal annualInterestRate;
  public final BigDecimal monthlyPaymentAmount;
  public final List<Payment> payments;

  public AmortizationSchedule(BigDecimal loanAmount, int durationInMonths, BigDecimal annualInterestRate, BigDecimal monthlyPaymentAmount, List<Payment> payments) {
    this.loanAmount = loanAmount;
    this.durationInMonths = durationInMonths;
    this.annualInterestRate = annualInterestRate;
    this.monthlyPaymentAmount = monthlyPaymentAmount;
    this.payments = payments;
  }

}
