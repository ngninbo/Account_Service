package account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotEmpty(message = "employee email must not be empty")
    private String employee;
    @Pattern(regexp = "\\d{2}-\\d{4}", message = "Period has invalid format")
    private String period;
    @NotNull(message = "salary is incorrect")
    private Long salary;

    @AssertTrue(message = "payment period is not valid")
    public boolean isValidPeriod() {

        if (this.period == null) {
            return false;
        }

        int month = Integer.parseInt(period.split("-")[0]);
        return month > 0 && month < 12;
    }

    @AssertTrue(message = "Salary can not be negativ")
    public boolean validateSalary() {
        return this.salary != null && this.salary >= 0;
    }
}
