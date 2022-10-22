package account.service.payment;

import account.domain.payment.PaymentDto;
import account.domain.payment.PaymentResponse;
import account.model.payment.PaymentRequest;
import account.exception.payment.PasswordUpdateException;
import account.exception.payment.PaymentNotFoundException;
import account.exception.payment.PaymentSavingException;

import java.util.List;

public interface IPaymentService {

    List<PaymentDto> findAllByEmail(String email);
    PaymentDto findByEmailAndPeriod(String email, String period) throws PaymentNotFoundException;
    PaymentResponse save(List<PaymentRequest> payments) throws PasswordUpdateException, PaymentSavingException;
    PaymentResponse save(PaymentRequest payment) throws PasswordUpdateException, PaymentSavingException;
}
