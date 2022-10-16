package account.mapper;

import account.domain.PaymentDto;
import account.model.Payment;
import account.model.PaymentRequest;
import account.model.User;
import account.service.UserService;
import account.util.PaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    private final UserService userService;

    @Autowired
    public PaymentMapper(UserService userService) {
        this.userService = userService;
    }

    public Payment mapToPayment(PaymentRequest request) throws NoSuchElementException {


        final Optional<User> employee = userService.findByEmail(request.getEmployee());
        return Payment.builder()
                .employee(employee.orElseThrow())
                .period(request.getPeriod())
                .salary(request.getSalary())
                .build();
    }

    public PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder().name(payment.getEmployee().getName())
                .lastname(payment.getEmployee().getLastname())
                .period(PaymentUtil.convertMonthFromPeriodToString(payment.getPeriod()))
                .salary(PaymentUtil.getFullSalary(payment.getSalary()))
                .build();
    }

    public List<PaymentDto> mapToList(List<Payment> payments) {
        return payments.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
