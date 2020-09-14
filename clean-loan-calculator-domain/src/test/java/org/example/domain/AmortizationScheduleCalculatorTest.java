package org.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AmortizationScheduleCalculatorTest {

  public static final double DELTA = 0.01;
  private BigDecimal loanAmount;
  private BigDecimal interestRate;

  @BeforeEach
  void setUp() {
    loanAmount = BigDecimal.ZERO;
    interestRate = new BigDecimal("0.1");
  }

  @Test
  void whenLoanAmountIs0_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(loanAmount, 1, interestRate));
  }

  @Test
  void whenLoanAmountIsNegative_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(new BigDecimal("-1"), 1, interestRate));
  }

  @Test
  void whenDurationIs0_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(new BigDecimal("1"), 0, interestRate));
  }

  @Test
  void whenDurationIsNegative_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(new BigDecimal("1"), -1, interestRate));
  }

  @Test
  void whenInterestRateIs0_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(new BigDecimal("1"), 1, new BigDecimal("0")));
  }

  @Test
  void whenInterestRateIsNegative_shouldThrowException() {
    assertThrows(IllegalArgumentException.class,
      () -> new AmortizationScheduleCalculator(new BigDecimal("1"), 1, new BigDecimal("-1")));
  }

  @Nested
  class WithLoanAmountOf1000 {

    @BeforeEach
    void setUp() {
      loanAmount = new BigDecimal(1000);
    }

    @Test
    void withDurationOfOneMonth_scheduleShouldBeOnePayment() {
      AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 1, interestRate);

      AmortizationSchedule schedule = calculator.execute();

      assertAmountEquals(interestRate, schedule.annualInterestRate);
      assertAmountEquals(loanAmount, schedule.loanAmount);
      assertAmountEquals(1008.33, schedule.monthlyPaymentAmount);
      assertThat(schedule.durationInMonths, is(1));

      List<Payment> payments = schedule.payments;
      assertThat(payments, hasSize(1));

      Payment theOnlyPayment = payments.get(0);
      BigDecimal actualAmount = theOnlyPayment.interestAmount;
      assertAmountEquals(8.33, actualAmount);
      assertAmountEquals(1000, theOnlyPayment.principalAmount);
      assertAmountEquals(0, theOnlyPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, theOnlyPayment.totalPaidPrincipalAmount);
    }



    @Test
    void withDurationOfTwoMonths_scheduleShouldBeTwoPayments() {
      AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 2, interestRate);

      AmortizationSchedule schedule = calculator.execute();

      assertAmountEquals(interestRate, schedule.annualInterestRate);
      assertAmountEquals(loanAmount, schedule.loanAmount);
      assertAmountEquals(506.25, schedule.monthlyPaymentAmount);
      assertThat(schedule.durationInMonths, is(2));

      List<Payment> payments = schedule.payments;
      assertThat(payments, hasSize(2));

      Payment firstPayment = payments.get(0);
      assertAmountEquals(8.33, firstPayment.interestAmount);
      assertAmountEquals(497.92, firstPayment.principalAmount);
      assertAmountEquals(502.07, firstPayment.remainingPrincipalAmount);
      assertAmountEquals(497.92, firstPayment.totalPaidPrincipalAmount);

      Payment secondPayment = payments.get(1);
      assertAmountEquals(4.18, secondPayment.interestAmount);
      assertAmountEquals(502.07, secondPayment.principalAmount);
      assertAmountEquals(0, secondPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, secondPayment.totalPaidPrincipalAmount);
    }

    @Test
    void withDurationOfThreeMonths_scheduleShouldBeThreePayments() {
      AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 3, interestRate);

      AmortizationSchedule schedule = calculator.execute();

      assertAmountEquals(interestRate, schedule.annualInterestRate);
      assertAmountEquals(loanAmount, schedule.loanAmount);
      assertAmountEquals(338.90, schedule.monthlyPaymentAmount);
      assertThat(schedule.durationInMonths, is(3));

      List<Payment> payments = schedule.payments;
      assertThat(payments, hasSize(3));

      Payment firstPayment = payments.get(0);
      assertAmountEquals(8.33, firstPayment.interestAmount);
      assertAmountEquals(330.57, firstPayment.principalAmount);
      assertAmountEquals(669.43, firstPayment.remainingPrincipalAmount);
      assertAmountEquals(330.57, firstPayment.totalPaidPrincipalAmount);

      Payment secondPayment = payments.get(1);
      assertAmountEquals(5.57, secondPayment.interestAmount);
      assertAmountEquals(333.32, secondPayment.principalAmount);
      assertAmountEquals(336.10, secondPayment.remainingPrincipalAmount);
      assertAmountEquals(663.90, secondPayment.totalPaidPrincipalAmount);

      Payment thirdPayment = payments.get(2);
      assertAmountEquals(2.80, thirdPayment.interestAmount);
      assertAmountEquals(336.10, thirdPayment.principalAmount);
      assertAmountEquals(0, thirdPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, thirdPayment.totalPaidPrincipalAmount);
    }

  }

  private void assertAmountEquals(double expectedAmount, BigDecimal actualAmount) {
    assertThat(actualAmount, closeTo(BigDecimal.valueOf(expectedAmount), BigDecimal.valueOf(DELTA)));
  }

  private void assertAmountEquals(BigDecimal expectedAmount, BigDecimal actualAmount) {
    assertThat(actualAmount, closeTo(expectedAmount, BigDecimal.valueOf(DELTA)));
  }

}
