package account.service;

import account.domain.PaymentDto;
import account.domain.PaymentResponse;
import account.mapper.PaymentMapper;
import account.model.Payment;
import account.model.PaymentRequest;
import account.repository.PaymentRepository;
import account.util.exception.PaymentNotFoundException;
import account.util.exception.PaymentSavingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        return payments.isEmpty() ? List.of() : payments.stream().map(paymentMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> findByEmailAndPeriod(String email, String period) throws PaymentNotFoundException {

        try {
            var  payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(email, period);
            if (payment.isEmpty()) {
                return List.of();
            }
            return List.of(paymentMapper.mapToDto(payment.get()));
        } catch (Exception e) {
            throw new PaymentNotFoundException("Payment not found!: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse save(List<PaymentRequest> payments) throws PaymentSavingException {
        try {
            for(PaymentRequest request: payments) {
                var payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(request.getEmployee(), request.getPeriod());
                if (payment.isEmpty()) {
                    paymentRepository.save(paymentMapper.mapToPayment(request));
                } else {
                    throw new PaymentSavingException("Period must be unique!");
                }
            }
            return new PaymentResponse();
        } catch (Exception e) {
            throw new PaymentSavingException("An Error occurred while updating!: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse save(PaymentRequest request) throws PaymentSavingException {

        try {
            var payment = paymentRepository.findByEmployee_EmailIgnoreCaseAndPeriod(
                    request.getEmployee(), request.getPeriod());
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
}
