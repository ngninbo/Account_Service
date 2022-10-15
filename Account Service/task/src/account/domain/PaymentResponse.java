package account.domain;

import java.util.Objects;

public class PaymentResponse {

    private static final String DEFAULT_STATUS = "Added successfully!";

    private String status;

    {
        this.status = DEFAULT_STATUS;
    }

    public PaymentResponse() {

    }

    public PaymentResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentResponse)) return false;
        PaymentResponse that = (PaymentResponse) o;
        return Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus());
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "status='" + status + '\'' +
                '}';
    }
}
