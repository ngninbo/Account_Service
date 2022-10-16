package account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PasswordChangeResponse {

    public static final String DEFAULT_STATUS = "The password has been updated successfully";

    private String email;
    private String status;
}
