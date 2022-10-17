package account.controller;

import account.domain.PaymentResponse;
import account.model.PaymentRequest;
import account.service.PaymentService;
import account.util.PaymentUtil;
import account.util.exception.PaymentNotFoundException;
import account.util.exception.PaymentSavingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "/acct/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> update(@Valid @RequestBody List<PaymentRequest> payrolls) throws PaymentSavingException {
        return ResponseEntity.ok(paymentService.save(payrolls));
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<PaymentResponse> update(@Valid @RequestBody PaymentRequest request) throws PaymentSavingException {
        return ResponseEntity.ok(this.paymentService.save(request));
    }

    @GetMapping(path = "/empl/payment")
    public ResponseEntity<?> getPayroll(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam(required = false) String period) throws PaymentNotFoundException, PaymentSavingException {

        if (period == null) {
            return ResponseEntity.ok(paymentService.findAllByEmail(userDetails.getUsername()));
        } else if (PaymentUtil.isPeriodValid().test(period)) {
            return ResponseEntity.ok(paymentService.findByEmailAndPeriod(userDetails.getUsername(), period));
        } else {
            throw new PaymentSavingException("Wrong date in request!");
        }
    }
}
