package hu.restapp.retailloan;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Interest {
    BigDecimal interestAmount;
    BigDecimal interestWash;
}
