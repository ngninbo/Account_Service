package account.util;

import java.util.Set;

public class Blacklist {

    public static final Set<String> passwords = Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public static boolean contains(String password) {
        return passwords.contains(password);
    }
}
