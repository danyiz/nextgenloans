package hu.restapp.retailloan.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrencyDAO {
    @Autowired
    CurrencyRepository currencyRepository;

    private static final String BASE_CURRENCY = "HUF";
    private static final String BASE_COUNTRY = "HU";
    private static final String BASE_DESCRIPTION = "MAGYAR_FORINT";
    private static final int BASE_DECIMAL = 0;

    public Currency getCurrency(String currencyCode) throws CurrencyNotExistsException{
        Optional<Currency> currency = currencyRepository.findById(currencyCode);
        if(!currency.isPresent()) {
            if(currencyCode.equals(BASE_CURRENCY)){
                Currency currency1 = new Currency();
                currency1.setCurrencyCode(BASE_CURRENCY);
                currency1.setCountryCode(BASE_COUNTRY);
                currency1.setCurrencyDecimals(BASE_DECIMAL);
                currency1.setCurrencyDescription(BASE_DESCRIPTION);
                currencyRepository.save(currency1);
                currency = currencyRepository.findById(currencyCode);
                if(!currency.isPresent()){
                    throw new CurrencyNotExistsException();
                }
            }else
            {
                throw new CurrencyNotExistsException();
            }
        }
        return currency.get();
    }
}
