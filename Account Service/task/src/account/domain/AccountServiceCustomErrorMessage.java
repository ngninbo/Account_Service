package account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountServiceCustomErrorMessage {

    public static String DEFAULT_MESSAGE = "User exist!";

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    {
        this.message = DEFAULT_MESSAGE;
    }
}
