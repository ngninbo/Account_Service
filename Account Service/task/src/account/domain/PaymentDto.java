package account.domain;

import java.util.Objects;

public class PaymentDto {

    private String name;
    private String lastname;
    private String period;
    private String salary;

    public PaymentDto(String name, String lastname, String period, String salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = period;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentDto)) return false;
        PaymentDto that = (PaymentDto) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getLastname(), that.getLastname()) && Objects.equals(getPeriod(), that.getPeriod()) && Objects.equals(getSalary(), that.getSalary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLastname(), getPeriod(), getSalary());
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", period='" + period + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
