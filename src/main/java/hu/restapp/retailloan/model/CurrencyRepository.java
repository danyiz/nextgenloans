package hu.restapp.retailloan.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface CurrencyRepository extends JpaRepository<Currency,String> {

}
