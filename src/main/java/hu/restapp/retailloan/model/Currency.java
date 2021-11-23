package hu.restapp.retailloan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Currency")
public class Currency {
    @Id
    @Column
    String currencyCode;
    @Column
    String currencyDescription;
    @Column
    String countryCode;
    @Column
    Integer currencyDecimals;
}
