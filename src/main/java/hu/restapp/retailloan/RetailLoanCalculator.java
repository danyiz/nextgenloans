package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanSchedule;
import hu.restapp.retailloan.model.RetailLoanScheduleRepository;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@NoArgsConstructor
public class RetailLoanCalculator {

    @Autowired
    private RetailLoanScheduleRepository retailLoanScheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TriggerDateUtility triggerDateUtility;

    @Autowired
    RegularPaymentCalculatorFactoryInterface regularPaymentCalculatorFactory;

    @Autowired
    InterestCalculatorFactoryInterface interestCalculatorFactory;


    public List<ScheduleDTO> loanShed = new ArrayList<>();
    private int Base = 360;
    private BigDecimal loanPrincipalAmount;
    private BigDecimal loanInterestRate;
    private int numberOfPayments;

    InterestCalculatorTypes interestCalculatorType = InterestCalculatorTypes.BASIC;
    RegularPaymentCalculatorTypes regularPaymentCalculatorType = RegularPaymentCalculatorTypes.BASIC;

    public BigDecimal calculateLoanInterestPartForPeriod(BigDecimal principalAmount, BigDecimal loanInterestRate, Integer interestBase, Long numberOfDays)
    {
        InterestCalculatorInterface interestCalculatorInterface = interestCalculatorFactory.createInterestCalculator(interestCalculatorType);
        return interestCalculatorInterface.calculateInterestForPeriod(principalAmount, loanInterestRate,numberOfDays, interestBase).getInterestAmount().round(new MathContext(5));
    }

    public BigDecimal calculateRegularPayment(){
        RegularPaymentCalculatorInterface regularPaymentCalculatorInterface = regularPaymentCalculatorFactory.createRegularPaymentCalculator(regularPaymentCalculatorType);
        return regularPaymentCalculatorInterface.calculateRegularPayment(loanInterestRate,loanPrincipalAmount,numberOfPayments,BigDecimal.valueOf(365/360));
    }

   public void generateSchedule (boolean persistScheduleToDB) {

        LocalDate valueDate = LocalDate.now();
        BigDecimal outstandingPrincipalAmount = loanPrincipalAmount;

        BigDecimal regularPayment = calculateRegularPayment();

        for(int i = 1; i< numberOfPayments +1; i ++) {

            valueDate = triggerDateUtility.calcNextDate(1,valueDate,valueDate.getDayOfMonth());

            BigDecimal interestForPeriod = calculateLoanInterestPartForPeriod(outstandingPrincipalAmount, loanInterestRate,this.Base,30L);

            BigDecimal principalAmountPartForPeriod = regularPayment.subtract(interestForPeriod);

            if (principalAmountPartForPeriod.longValue() > outstandingPrincipalAmount.longValue()) {
                // in last payment the whole principal
                principalAmountPartForPeriod = outstandingPrincipalAmount;
            }

            ScheduleDTO scheduleItem = new ScheduleDTO(valueDate,"111111111111",i,
                    outstandingPrincipalAmount,
                    interestForPeriod,
                    principalAmountPartForPeriod,
                    regularPayment,
                    loanInterestRate,
                    BigDecimal.valueOf(0L),
                    BigDecimal.valueOf(0L),
                    BigDecimal.valueOf(0L));
            loanShed.add(scheduleItem);

            if(persistScheduleToDB){
                RetailLoanSchedule scheduleItemToPersist =  modelMapper.map(scheduleItem, RetailLoanSchedule.class);
                scheduleItemToPersist.setDailySequence(1L);
                retailLoanScheduleRepository.save(scheduleItemToPersist);
            }

            outstandingPrincipalAmount = outstandingPrincipalAmount.subtract(principalAmountPartForPeriod);
        }
    }

}
