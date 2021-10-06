package hu.restapp.retailloan.model;

import hu.restapp.retailloan.InterestCalculatorTypes;
import hu.restapp.retailloan.RegularPaymentCalculatorTypes;
import hu.restapp.retailloan.RegularPaymentSplitterTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RetailLoanAttributes {

    InterestCalculatorTypes interestCalculatorType = InterestCalculatorTypes.BASIC;
    RegularPaymentCalculatorTypes regularPaymentCalculatorType = RegularPaymentCalculatorTypes.BASIC;
    RegularPaymentCalculatorTypes regularPaymentCalculatorTypeBase = RegularPaymentCalculatorTypes.BASIC;
    RegularPaymentCalculatorTypes delayedPrincipalPaymentCalculatorType = RegularPaymentCalculatorTypes.PRINCIPAL_DELAYED;
    RegularPaymentSplitterTypes regularPaymentSplitterTypes = RegularPaymentSplitterTypes.BASIC;
    RegularPaymentSplitterTypes regularPaymentSplitterTypesBase = RegularPaymentSplitterTypes.BASIC;
    RegularPaymentSplitterTypes delayedPrincipalRegularPaymentSplitterType = RegularPaymentSplitterTypes.PRINCIPAL_DELAYED;

    @NotNull
    private String accountNumber;
    @Pattern(regexp ="(?:(?:^|,)(360|365|366))+$")
    private Integer calculationBasis;
    @NotNull
    @Pattern(regexp = "[0-9]*\\.?[0-9]*[1-9]")
    private BigDecimal loanPrincipalAmount;
    @NotNull
    private BigDecimal loanInterestRate;
    @NotNull
    @Pattern(regexp = "[0-9]*\\.?[0-9]*[1-9]")
    private Integer numberOfPayments;
    @NotNull
    private Integer nextPaymentNumber;
    @NotNull
    private BigDecimal bonusInterestRate;
    @NotNull
    private BigDecimal subsidizedInterestRate;
    @NotNull
    private BigDecimal correctionCoefficient;
    @NotNull
    private Integer daysInPeriod;
    @NotNull
    private LocalDate valueDate;
    @NotNull
    private String currencyCode;
    @NotNull
    private Integer payDay;
    @NotNull
    private Integer settlementFrequencyInMoths;
    @NotNull
    @Pattern(regexp = "[0-9]")
    private Integer termsToDelayPrincipalPayment;
    @NotNull
    private BigDecimal regularPayment;
    @NotNull
    private BigDecimal interestPart;
    @NotNull
    private BigDecimal principalPart;
    @NotNull
    private BigDecimal managementFeeMonthlyAmount;

    private int currencyDecimals = 0;
    private RoundingMode currencyRoundingMode = RoundingMode.HALF_UP;

}
