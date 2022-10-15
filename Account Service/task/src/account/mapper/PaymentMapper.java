package account.mapper;

import account.domain.PaymentDto;
import account.model.Payment;
import account.model.PaymentRequest;
import account.model.User;
import account.service.UserService;
import account.util.PaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PaymentMapper {

    private final UserService userService;

    @Autowired
    public PaymentMapper(UserService userService) {
        this.userService = userService;
    }

    public Payment mapToPayment(PaymentRequest request) throws NoSuchElementException {


        final Optional<User> employee = userService.findByEmail(request.getEmployee());
        return new Payment(
                employee.orElseThrow(),
                request.getPeriod(),
                request.getSalary());
    }

    public PaymentDto mapToDto(Payment payment) {
        return new PaymentDto(payment.getEmployee().getName(),
                payment.getEmployee().getLastname(),
                PaymentUtil.convertMonthFromPeriodToString(payment.getPeriod()),
                PaymentUtil.getFullSalary(payment.getSalary()));
    }
}
