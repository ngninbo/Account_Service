package account.repository;

import account.model.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findAllByEmployee_EmailIgnoreCaseOrderByPeriodDesc(String email);

    Optional<Payment> findByEmployee_EmailIgnoreCaseAndPeriod(String employee, String period);
}
