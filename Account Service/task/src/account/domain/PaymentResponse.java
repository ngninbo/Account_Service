package account.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private static final String DEFAULT_STATUS = "Added successfully!";

    private String status;

    {
        this.status = DEFAULT_STATUS;
    }
}
