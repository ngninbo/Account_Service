package account.model.user;

import java.util.stream.Stream;

public enum Role {

    ACCOUNTANT,
    ADMINISTRATOR,
    AUDITOR,
    USER;

    public static String[] getChangePassRoles() {
        return Stream.of(USER, ACCOUNTANT, ADMINISTRATOR).map(Enum::name).toArray(String[]::new);
    }

    public static String[] getPaymentRoles() {
        return Stream.of(USER, ACCOUNTANT).map(Enum::name).toArray(String[]::new);
    }

    public String getName() {
        return "ROLE_".concat(name());
    }
}
