package hu.restapp.retailloan;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.restapp.retailloan.model.*;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class RetailLoanCalculator {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RetailLoanScheduleRepository retailLoanScheduleRepository;

    @Autowired
    private TriggerDateUtility triggerDateUtility;

    @Autowired
    RegularPaymentCalculatorFactoryInterface regularPaymentCalculatorFactory;

    @Autowired
    InterestCalculatorFactoryInterface interestCalculatorFactory;

    @Autowired
    RegularPaymentSplitterFactory regularPaymentSplitterFactory;

    public BigDecimal calculateLoanInterestPartForPeriod(RetailLoanAttributes retailLoanAttributes)
    {
        InterestCalculatorInterface interestCalculatorInterface = interestCalculatorFactory.createInterestCalculator(retailLoanAttributes.getInterestCalculatorType());
        return interestCalculatorInterface.calculateInterestForPeriod(retailLoanAttributes).getInterestAmount().setScale(0, RoundingMode.FLOOR);
    }

    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes){

        if(retailLoanAttributes.getTermsToDelayPrincipalPayment()>retailLoanAttributes.getNextPaymentNumber()){
            retailLoanAttributes.setRegularPaymentCalculatorType(retailLoanAttributes.getDelayedPrincipalPaymentCalculatorType());
        }else
        {
            retailLoanAttributes.setRegularPaymentCalculatorType(retailLoanAttributes.getRegularPaymentCalculatorTypeBase());
        }

        RegularPaymentCalculatorInterface regularPaymentCalculatorInterface = regularPaymentCalculatorFactory.createRegularPaymentCalculator(retailLoanAttributes.getRegularPaymentCalculatorType());

        return regularPaymentCalculatorInterface.calculateRegularPayment(retailLoanAttributes);
    }

    public RetailLoanAttributes splitRegularPayment(RetailLoanAttributes retailLoanAttributes){

        if(retailLoanAttributes.getTermsToDelayPrincipalPayment()>retailLoanAttributes.getNextPaymentNumber()){
           retailLoanAttributes.setRegularPaymentSplitterTypes(retailLoanAttributes.getDelayedPrincipalRegularPaymentSplitterType());
        }else
        {
            retailLoanAttributes.setRegularPaymentSplitterTypes(retailLoanAttributes.getRegularPaymentSplitterTypesBase());
        }

        RegularPaymentSplitterInterface regularPaymentSplitterFactoryInterface = regularPaymentSplitterFactory.createRegularPaymentSplitter(retailLoanAttributes.getRegularPaymentSplitterTypes());

        return regularPaymentSplitterFactoryInterface.splitRegularPayment(retailLoanAttributes);
    }

   public List<RetailLoanScheduleDTO> generateSchedule (boolean persistScheduleToDB,RetailLoanAttributes retailLoanAttributes) {

        List<RetailLoanScheduleDTO> loanShed = new ArrayList<>();
        retailLoanAttributes.setValueDate(LocalDate.now());
        retailLoanAttributes.setNextPaymentNumber(1);
        retailLoanAttributes.setRegularPayment(calculateRegularPayment(retailLoanAttributes));


        for(int i = 1; i< retailLoanAttributes.getNumberOfPayments() +1; i ++) {

            retailLoanAttributes.setNextPaymentNumber(i);
            retailLoanAttributes.setValueDate(triggerDateUtility.calcNextDate(retailLoanAttributes.getSettlementFrequencyInMoths(),
                                                                                retailLoanAttributes.getValueDate(),
                                                                                retailLoanAttributes.getPayDay()));
            // Period start date
            // Period end date
            // Days in period

            // Interest change array
            // Early repayment array

            // Generate Eventschema

            retailLoanAttributes.setInterestPart(calculateLoanInterestPartForPeriod(retailLoanAttributes));

            if ((retailLoanAttributes.getPrincipalPart().compareTo(retailLoanAttributes.getLoanPrincipalAmount()) >=0) ||
                    (retailLoanAttributes.getNextPaymentNumber().equals(retailLoanAttributes.getNumberOfPayments())))
            {
                // in last payment the whole principal
                retailLoanAttributes.setPrincipalPart(retailLoanAttributes.getLoanPrincipalAmount());
            }
            else
            {
                retailLoanAttributes = splitRegularPayment(retailLoanAttributes);
            }

            retailLoanAttributes.setLoanPrincipalAmount(retailLoanAttributes.getLoanPrincipalAmount().subtract(retailLoanAttributes.getPrincipalPart()));

            RetailLoanSchedule scheduleItem = objectMapper.convertValue(retailLoanAttributes, RetailLoanSchedule.class);
            RetailLoanScheduleDTO scheduleDTO =  objectMapper.convertValue(scheduleItem, RetailLoanScheduleDTO.class);

            loanShed.add(scheduleDTO);

            if(persistScheduleToDB){
                retailLoanScheduleRepository.save(scheduleItem);
            }

            if(retailLoanAttributes.getTermsToDelayPrincipalPayment()>0 && retailLoanAttributes.getTermsToDelayPrincipalPayment().equals(retailLoanAttributes.getNextPaymentNumber())){
                retailLoanAttributes.setNextPaymentNumber(retailLoanAttributes.getNextPaymentNumber()+1);
                log.info("Recalculating regular payment: {}",retailLoanAttributes);
                retailLoanAttributes.setRegularPayment(calculateRegularPayment(retailLoanAttributes));
            }
        }
        return loanShed;
    }
}
