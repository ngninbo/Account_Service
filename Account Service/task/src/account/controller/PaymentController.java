package account.controller;

import account.domain.AccountServiceCustomErrorMessage;
import account.domain.payment.PaymentResponse;
import account.model.payment.PaymentRequest;
import account.service.payment.PaymentServiceImpl;
import account.util.PaymentUtil;
import account.exception.payment.PaymentSavingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment service", description = "Payment management")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "/acct/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add list of payments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentRequest.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Duplicated entry in payment list | Salary can not be negativ! | payment period is not valid! | Period must be unique!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Access denied",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),})
    public ResponseEntity<PaymentResponse> update(@Valid @RequestBody List<PaymentRequest> payrolls) {
        return ResponseEntity.ok(paymentService.save(payrolls));
    }

    @PutMapping("/acct/payments")
    @Operation(description = "Add or update payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)) }),
            @ApiResponse(responseCode = "401",
                    description = "Access denied",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Salary can not be negativ! | payment period is not valid!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) })})
    public ResponseEntity<PaymentResponse> update(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(this.paymentService.save(request));
    }

    @GetMapping(path = "/empl/payment")
    @Operation(description = "Get list of payments by period (optional)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong date in request!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(responseCode = "401",
                    description = "Access denied",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Payment not found for given period!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }
            )})
    public ResponseEntity<?> getPayroll(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam(required = false) String period) {

        if (period == null) {
            return ResponseEntity.ok(paymentService.findAllByEmail(userDetails.getUsername()));
        } else if (PaymentUtil.isPeriodValid().test(period)) {
            return ResponseEntity.ok(paymentService.findByEmailAndPeriod(userDetails.getUsername(), period));
        } else {
            throw new PaymentSavingException("Wrong date in request!");
        }
    }
}
