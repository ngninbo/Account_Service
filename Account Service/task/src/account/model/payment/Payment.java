package account.model.payment;

import account.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "sequencePayment", sequenceName = "PaymentSeq")
@Table(name = "emp_payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequencePayment")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "employee email must not be empty")
    private User employee;
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

    @AssertFalse(message = "Salary can not be negativ")
    public boolean validateSalary() {
        return this.salary != null && this.salary < 0L;
    }
}
