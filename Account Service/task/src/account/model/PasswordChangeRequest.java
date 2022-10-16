package account.model;

import account.util.Blacklist;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PasswordChangeRequest {

    @JsonProperty("new_password")
    @NotNull
    private String password;

    @AssertFalse(message = "The password is in the hacker's database!")
    public boolean isBreached() {
        return this.password != null && Blacklist.contains(this.password);
    }

    @AssertTrue(message = "Password length must be 12 chars minimum!")
    public boolean hasValideLength() {
        return this.password != null && this.password.length() >= 12;
    }


    public PasswordChangeRequest() {
        super();
    }

    public PasswordChangeRequest(String password) {
        this();
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordChangeRequest)) return false;
        PasswordChangeRequest that = (PasswordChangeRequest) o;
        return Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword());
    }

    @Override
    public String toString() {
        return "PasswordChangeRequest{" +
                "password='" + password + '\'' +
                '}';
    }
}
