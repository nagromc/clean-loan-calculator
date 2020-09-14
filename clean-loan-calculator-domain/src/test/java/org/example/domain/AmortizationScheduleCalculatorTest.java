package org.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
  void whenLoanAmountIs0_scheduleShouldBeEmpty() {
    AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 1, interestRate);

    List<Payment> schedule = calculator.execute();

    assertThat(schedule, is(empty()));
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

      List<Payment> schedule = calculator.execute();

      assertThat(schedule, hasSize(1));
      Payment theOnlyPayment = schedule.get(0);
      double expectedAmount = 8.33;
      BigDecimal actualAmount = theOnlyPayment.interestAmount;
      assertAmountEquals(expectedAmount, actualAmount);
      assertAmountEquals(1000, theOnlyPayment.principalAmount);
      assertAmountEquals(0, theOnlyPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, theOnlyPayment.totalPaidPrincipalAmount);
    }



    @Test
    void withDurationOfTwoMonths_scheduleShouldBeTwoPayments() {
      AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 2, interestRate);

      List<Payment> schedule = calculator.execute();

      assertThat(schedule, hasSize(2));

      Payment firstPayment = schedule.get(0);
      assertAmountEquals(8.33, firstPayment.interestAmount);
      assertAmountEquals(497.92, firstPayment.principalAmount);
      assertAmountEquals(502.07, firstPayment.remainingPrincipalAmount);
      assertAmountEquals(497.92, firstPayment.totalPaidPrincipalAmount);

      Payment secondPayment = schedule.get(1);
      assertAmountEquals(4.18, secondPayment.interestAmount);
      assertAmountEquals(502.07, secondPayment.principalAmount);
      assertAmountEquals(0, secondPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, secondPayment.totalPaidPrincipalAmount);
    }

    @Test
    void withDurationOfThreeMonths_scheduleShouldBeThreePayments() {
      AmortizationScheduleCalculator calculator = new AmortizationScheduleCalculator(loanAmount, 3, interestRate);

      List<Payment> schedule = calculator.execute();

      assertThat(schedule, hasSize(3));

      Payment firstPayment = schedule.get(0);
      assertAmountEquals(8.33, firstPayment.interestAmount);
      assertAmountEquals(330.57, firstPayment.principalAmount);
      assertAmountEquals(669.43, firstPayment.remainingPrincipalAmount);
      assertAmountEquals(330.57, firstPayment.totalPaidPrincipalAmount);

      Payment secondPayment = schedule.get(1);
      assertAmountEquals(5.57, secondPayment.interestAmount);
      assertAmountEquals(333.32, secondPayment.principalAmount);
      assertAmountEquals(336.10, secondPayment.remainingPrincipalAmount);
      assertAmountEquals(663.90, secondPayment.totalPaidPrincipalAmount);

      Payment thirdPayment = schedule.get(2);
      assertAmountEquals(2.80, thirdPayment.interestAmount);
      assertAmountEquals(336.10, thirdPayment.principalAmount);
      assertAmountEquals(0, thirdPayment.remainingPrincipalAmount);
      assertAmountEquals(1000, thirdPayment.totalPaidPrincipalAmount);
    }

  }

  private void assertAmountEquals(double expectedAmount, BigDecimal actualAmount) {
    assertThat(actualAmount, closeTo(BigDecimal.valueOf(expectedAmount), BigDecimal.valueOf(DELTA)));
  }

}
