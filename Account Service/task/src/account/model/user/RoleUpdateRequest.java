package account.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    @NotEmpty
    @JsonProperty("user")
    private String email;
    private String role;
    private String operation;
}
