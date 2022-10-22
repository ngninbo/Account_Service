package account.model.user;

import account.util.Blacklist;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
