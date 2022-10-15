package account.util;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Predicate;

public class PaymentUtil {

    private static final int CENT = 100;

    public static String getFullSalary(Long salary) {
        return String.format("%s dollar(s) %s cent(s)", salary/ CENT,salary % CENT);
    }

    public static String convertMonthFromPeriodToString(String period) {
        String[] periodArr = period.split("-");
        int month = Integer.parseInt(periodArr[0]);

        return String.format("%s-%s", Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE ,
                Locale.US), periodArr[1]);
    }

    public static Predicate<String> isPeriodValid() {
        return period -> {
            if (!period.matches("\\d{2}-\\d{4}")) {
                return false;
            }
            final int i = Integer.parseInt(period.split("-")[0]);
            return i >= 0 && i <= 12;
        };
    }
}
