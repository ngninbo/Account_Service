package account.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Salary {

    private static final int PERCENT = 100;

    private final long dollar;

    private final long cent;

    public static Salary of(Long salary) {
        return new Salary(salary / PERCENT, salary % PERCENT);
    }
}
