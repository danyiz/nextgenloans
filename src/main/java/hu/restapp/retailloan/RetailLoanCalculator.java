package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanSchedule;
import hu.restapp.retailloan.model.RetailLoanScheduleRepository;
import hu.restapp.retailloan.model.RetailLoanScheduleDTO;
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


    public List<RetailLoanScheduleDTO> loanShed = new ArrayList<>();


    InterestCalculatorTypes interestCalculatorType = InterestCalculatorTypes.BASIC;
    RegularPaymentCalculatorTypes regularPaymentCalculatorType = RegularPaymentCalculatorTypes.BASIC;

    public BigDecimal calculateLoanInterestPartForPeriod(RetailLoanAttributes retailLoanAttributes)
    {
        InterestCalculatorInterface interestCalculatorInterface = interestCalculatorFactory.createInterestCalculator(interestCalculatorType);
        return interestCalculatorInterface.calculateInterestForPeriod(retailLoanAttributes).getInterestAmount();
    }

    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes){
        RegularPaymentCalculatorInterface regularPaymentCalculatorInterface = regularPaymentCalculatorFactory.createRegularPaymentCalculator(regularPaymentCalculatorType);
        return regularPaymentCalculatorInterface.calculateRegularPayment(retailLoanAttributes);
    }

   public void generateSchedule (boolean persistScheduleToDB,RetailLoanAttributes retailLoanAttributes) {

        LocalDate valueDate = LocalDate.now();
        BigDecimal outstandingPrincipalAmount = retailLoanAttributes.getLoanPrincipalAmount();

        BigDecimal regularPayment = calculateRegularPayment(retailLoanAttributes);

        for(int i = 1; i< retailLoanAttributes.getNumberOfPayments() +1; i ++) {

            valueDate = triggerDateUtility.calcNextDate(1,valueDate,valueDate.getDayOfMonth());

            BigDecimal interestForPeriod = calculateLoanInterestPartForPeriod(retailLoanAttributes);

            BigDecimal principalAmountPartForPeriod = regularPayment.subtract(interestForPeriod);
            retailLoanAttributes.setLoanPrincipalAmount(retailLoanAttributes.getLoanPrincipalAmount().subtract(principalAmountPartForPeriod));

            if (principalAmountPartForPeriod.longValue() > outstandingPrincipalAmount.longValue()) {
                // in last payment the whole principal
                principalAmountPartForPeriod = outstandingPrincipalAmount;
            }

            RetailLoanScheduleDTO scheduleItem = new RetailLoanScheduleDTO(valueDate,"111111111111",i,
                    retailLoanAttributes.getLoanPrincipalAmount(),
                    interestForPeriod,
                    principalAmountPartForPeriod,
                    regularPayment,
                    retailLoanAttributes.getLoanInterestRate(),
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
