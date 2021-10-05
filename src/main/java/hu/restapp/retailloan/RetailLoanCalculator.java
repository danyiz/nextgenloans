package hu.restapp.retailloan;

import hu.restapp.retailloan.model.*;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private RetailLoanScheduleRepository retailLoanScheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

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
            retailLoanAttributes.setRegularPaymentCalculatorType(RegularPaymentCalculatorTypes.PRINCIPAL_DELAYED);
        }else
        {
            retailLoanAttributes.setRegularPaymentCalculatorType(RegularPaymentCalculatorTypes.BASIC);
        }

        RegularPaymentCalculatorInterface regularPaymentCalculatorInterface = regularPaymentCalculatorFactory.createRegularPaymentCalculator(retailLoanAttributes.getRegularPaymentCalculatorType());

        return regularPaymentCalculatorInterface.calculateRegularPayment(retailLoanAttributes);
    }

    public RetailLoanAttributes splitRegularPayment(RetailLoanAttributes retailLoanAttributes){

        if(retailLoanAttributes.getTermsToDelayPrincipalPayment()>retailLoanAttributes.getNextPaymentNumber()){
           retailLoanAttributes.setRegularPaymentSplitterTypes(RegularPaymentSplitterTypes.PRINCIPAL_DELAYED);
        }else
        {
            retailLoanAttributes.setRegularPaymentSplitterTypes(RegularPaymentSplitterTypes.BASIC);
        }

        RegularPaymentSplitterInterface regularPaymentSplitterFactoryInterface = regularPaymentSplitterFactory.createRegularPaymentSplitter(retailLoanAttributes.getRegularPaymentSplitterTypes());

        return regularPaymentSplitterFactoryInterface.splitRegularPayment(retailLoanAttributes);
    }

   public List<RetailLoanScheduleDTO> generateSchedule (boolean persistScheduleToDB,RetailLoanAttributes retailLoanAttributes) {

        List<RetailLoanScheduleDTO> loanShed = new ArrayList<>();
        retailLoanAttributes.setValueDate(LocalDate.now());

        retailLoanAttributes.setRegularPayment(calculateRegularPayment(retailLoanAttributes));

        for(int i = 1; i< retailLoanAttributes.getNumberOfPayments() +1; i ++) {

            retailLoanAttributes.setNextPaymentNumber(i);
            retailLoanAttributes.setValueDate(triggerDateUtility.calcNextDate(retailLoanAttributes.getSettlementFrequencyInMoths(),
                                                                                retailLoanAttributes.getValueDate(),
                                                                                retailLoanAttributes.getPayDay()));

            retailLoanAttributes.setInterestPart(calculateLoanInterestPartForPeriod(retailLoanAttributes));

            if ((retailLoanAttributes.getPrincipalPart().longValue() >= retailLoanAttributes.getLoanPrincipalAmount().longValue()) ||
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

            RetailLoanSchedule scheduleItem = modelMapper.map(retailLoanAttributes, RetailLoanSchedule.class);
            RetailLoanScheduleDTO scheduleDTO =  modelMapper.map(scheduleItem, RetailLoanScheduleDTO.class);

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
