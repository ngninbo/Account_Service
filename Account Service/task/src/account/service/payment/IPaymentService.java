package account.service.payment;

import account.domain.payment.PaymentDto;
import account.domain.payment.PaymentResponse;
import account.model.payment.PaymentRequest;

import java.util.List;

public interface IPaymentService {

    List<PaymentDto> findAllByEmail(String email);
    PaymentDto findByEmailAndPeriod(String email, String period);
    PaymentResponse save(List<PaymentRequest> payments);
    PaymentResponse save(PaymentRequest payment);
}
