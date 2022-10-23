package account.service.payment;

import account.domain.payment.PaymentDto;
import account.domain.payment.PaymentResponse;
import account.mapper.PaymentMapper;
import account.model.payment.Payment;
import account.model.payment.PaymentRequest;
import account.repository.PaymentRepository;
import account.exception.payment.PaymentNotFoundException;
import account.exception.payment.PaymentSavingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public List<PaymentDto> findAllByEmail(String email) {
        final List<Payment> payments = this.paymentRepository.findAllByEmployee_EmailIgnoreCaseOrderByPeriodDesc(email);
        return payments.isEmpty() ? List.of() : paymentMapper.mapToList(payments);
    }

    @Override
    public PaymentDto findByEmailAndPeriod(String email, String period) throws PaymentNotFoundException {
        Payment  payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(email, period).orElseThrow(() -> new PaymentNotFoundException("Payment not found for given period!"));
        return paymentMapper.mapToDto(payment);
    }

    @Override
    @Transactional
    public PaymentResponse save(List<PaymentRequest> payments) throws PaymentSavingException {

        for (int i = 1; i < payments.size(); i++) {
            if (payments.get(i - 1).getPeriod().equals(payments.get(i).getPeriod())) {
                throw new PaymentSavingException("Duplicated entry in payment list");
            }
        }

        for(PaymentRequest request: payments) {
            Optional<Payment> payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(request.getEmail(), request.getPeriod());
            if (payment.isEmpty()) {
                validateRequest(request);
                paymentRepository.save(paymentMapper.mapToPayment(request));
            } else {
                throw new PaymentSavingException("Period must be unique!");
            }
        }
        return new PaymentResponse();
    }

    @Override
    @Transactional
    public PaymentResponse save(PaymentRequest request) throws PaymentSavingException {

        validateRequest(request);

        try {
            var payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(
                    request.getEmail(), request.getPeriod());
            if (payment.isEmpty()){
                paymentRepository.save(paymentMapper.mapToPayment(request));
            } else {
                var tmp = payment.get();
                tmp.setSalary(request.getSalary());
                paymentRepository.save(tmp);
            }

            return new PaymentResponse("Updated successfully!");
        } catch (Exception e) {
            throw new PaymentSavingException(e.getMessage());
        }
    }

    private boolean isValidPeriod(String period) {

        if (period == null) {
            return true;
        }

        int month = Integer.parseInt(period.split("-")[0]);
        return month <= 0 || month >= 12;
    }

    private void validateRequest(PaymentRequest request) throws PaymentSavingException {
        if (request.getSalary() < 0) {
            throw new PaymentSavingException("Salary can not be negativ!");
        }

        if (isValidPeriod(request.getPeriod())) {
            throw new PaymentSavingException("payment period is not valid!");
        }
    }
}
