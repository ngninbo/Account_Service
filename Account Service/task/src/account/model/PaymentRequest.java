package account.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class PaymentRequest {

    @NotEmpty(message = "employee email must not be empty")
    private String employee;
    @Pattern(regexp = "\\d{2}-\\d{4}", message = "Period has invalid format")
    private String period;
    @NotNull(message = "salary is incorrect")
    private Long salary;

    public PaymentRequest() {
    }

    public PaymentRequest(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentRequest)) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Objects.equals(getEmployee(), that.getEmployee()) && Objects.equals(getPeriod(), that.getPeriod()) && Objects.equals(getSalary(), that.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployee(), getPeriod(), getSalary());
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "employee='" + employee + '\'' +
                ", period='" + period + '\'' +
                ", salary=" + salary +
                '}';
    }
}
