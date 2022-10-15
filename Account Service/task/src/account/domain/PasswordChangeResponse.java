package account.domain;

import java.util.Objects;

public class PasswordChangeResponse {

    private static final String DEFAULT_STATUS = "The password has been updated successfully";

    private String email;
    private String status = DEFAULT_STATUS;

    public PasswordChangeResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof PasswordChangeResponse)) return false;
        PasswordChangeResponse that = (PasswordChangeResponse) o;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getStatus());
    }

    @Override
    public String toString() {
        return "PasswordChangeResponse{" +
                "email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
