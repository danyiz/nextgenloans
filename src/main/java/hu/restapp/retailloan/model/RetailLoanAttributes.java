package hu.restapp.retailloan.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
public class RetailLoanAttributes {

    @Pattern(regexp ="(?:(?:^|,)(360|365|366))+$")
    private Integer calculationBasis;
    @NotNull@Pattern(regexp = "[0-9]*\\.?[0-9]*[1-9]")
    private BigDecimal loanPrincipalAmount;
    @NotNull
    private BigDecimal loanInterestRate;
    @NotNull@Pattern(regexp = "[0-9]*\\.?[0-9]*[1-9]")
    private Integer numberOfPayments;
    @NotNull
    private BigDecimal bonusInterestRate;
    @NotNull
    private BigDecimal subsidizedInterestRate;
    @NotNull
    private BigDecimal correctionCoefficient;
    @NotNull
    private Integer daysInPeriod;
}
