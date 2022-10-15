package account.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "sequencePayment", sequenceName = "PaymentSeq")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequencePayment")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User employee;
    private String period;
    private Long salary;

    public Payment() {
    }

    public Payment(User employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) && Objects.equals(getEmployee(), payment.getEmployee()) && Objects.equals(getPeriod(), payment.getPeriod()) && Objects.equals(getSalary(), payment.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmployee(), getPeriod(), getSalary());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", employee=" + employee +
                ", period='" + period + '\'' +
                ", salary=" + salary +
                '}';
    }
}
