package org.example.domain;

import java.math.BigDecimal;

public class Payment {

  public final int periodNumber;
  public final BigDecimal principalAmount;
  public final BigDecimal interestAmount;
  public final BigDecimal remainingPrincipalAmount;
  public final BigDecimal totalPaidPrincipalAmount;

  public Payment(int periodNumber, BigDecimal principalAmount, BigDecimal interestAmount, BigDecimal remainingPrincipalAmount, BigDecimal totalPaidPrincipalAmount) {
    this.periodNumber = periodNumber;
    this.principalAmount = principalAmount;
    this.interestAmount = interestAmount;
    this.remainingPrincipalAmount = remainingPrincipalAmount;
    this.totalPaidPrincipalAmount = totalPaidPrincipalAmount;
  }

}
