package account.service;

import account.domain.PaymentDto;
import account.domain.PaymentResponse;
import account.model.PaymentRequest;
import account.util.exception.PasswordUpdateException;
import account.util.exception.PaymentNotFoundException;
import account.util.exception.PaymentSavingException;

import java.util.List;

public interface IPaymentService {

    List<PaymentDto> findAllByEmail(String email);
    List<PaymentDto> findByEmailAndPeriod(String email, String period) throws PaymentNotFoundException;
    PaymentResponse save(List<PaymentRequest> payments) throws PasswordUpdateException, PaymentSavingException;
    PaymentResponse save(PaymentRequest payment) throws PasswordUpdateException, PaymentSavingException;
}
