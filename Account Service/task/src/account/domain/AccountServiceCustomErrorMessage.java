package account.domain;

import lombok.*;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "{" +
                "timestamp:'" + timestamp + '\'' +
                ", status:" + status +
                ", error:'" + error + '\'' +
                ", message:'" + message + '\'' +
                ", path:'" + path + '\'' +
                '}';
    }
}
